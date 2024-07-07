package net.quantum.core;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.quantum.Quantum;
import net.quantum.core.events.QuantumLoadingFinishedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class ModuleLoader {
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String GLOBAL_NAME_ATTRIBUTE = "Module-Global-Name";
    public static final String DEPENDENCY_ATTRIBUTE = "Module-Dependencies";

    public static QuantumModule loadModule(Path path) throws IOException, ReflectiveOperationException {
        try (JarFile jar = new JarFile(path.toString())) {
            Manifest manifest = jar.getManifest();
            if (manifest != null) {
                Attributes attributes = manifest.getMainAttributes();
                String mainClassName = attributes.getValue(Attributes.Name.MAIN_CLASS);
                if (mainClassName != null) {
                    URL[] urls = {new URL("jar:file:" + path + "!/")};
                    try (URLClassLoader loader = new URLClassLoader(urls)) {
                        Class<?> mainClass = loader.loadClass(mainClassName);
                        Constructor<?> constructor = mainClass.getConstructor();
                        Object instance = constructor.newInstance();
                        if (instance instanceof QuantumModule module) {
                            String globalName = attributes.getValue(GLOBAL_NAME_ATTRIBUTE);
                            module.globalName = globalName != null ? globalName : module.name;
                            JsonElement dependenciesObject = JsonParser.parseString(attributes.containsKey(DEPENDENCY_ATTRIBUTE) ? attributes.getValue(DEPENDENCY_ATTRIBUTE) : "[]");
                            if (dependenciesObject == null) throw new RuntimeException("Malformed manifest: attribute '%s' must be a string representation of a JSON array (of strings)".formatted(DEPENDENCY_ATTRIBUTE));
                            if (!dependenciesObject.isJsonArray()) throw new RuntimeException("Malformed manifest: attribute '%s' must be a string representation of a JSON array (of strings)".formatted(DEPENDENCY_ATTRIBUTE));

                            module.dependencies = new ArrayList<>();

                            for (JsonElement element : dependenciesObject.getAsJsonArray().asList()) {
                                JsonPrimitive primitive;
                                if (!element.isJsonPrimitive() || !(primitive = element.getAsJsonPrimitive()).isString()) throw new RuntimeException("Malformed manifest: attribute '%s' must be a string representation of a JSON array (of strings)".formatted(DEPENDENCY_ATTRIBUTE));

                                module.dependencies.add(primitive.getAsString());
                            }

                            return module;
                        }
                    }
                }
            }
        }
        return null;
    }
    public static void registerAllModulesInDirectory(Path path, ModuleHandler handler) throws ReflectiveOperationException, IOException {
        List<QuantumModule> modules = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.jar")) {
            LOGGER.info("Loading modules...");

            for (Path fpath : stream) {
                LOGGER.info("Found jar: %s".formatted(fpath.getFileName().toString()));
                QuantumModule module = loadModule(fpath);
                if (module != null) {
                    LOGGER.info("Jar is valid.");
                    LOGGER.info("Loaded module: %s (global name: %s)".formatted(module.name, module.globalName));
                    modules.add(module);
                }
                else {
                    LOGGER.info("Jar is invalid, skipping");
                }
            }

            LOGGER.info("Module loading complete!");
        } catch (IOException e) {
            LOGGER.error("Unable to read modules directory.");
            e.printStackTrace();
        }

        LOGGER.info("Registering loaded modules...");

        for (QuantumModule module : modules) {
            module.register();
            handler.addModule(module);
        }

        LOGGER.info("All modules registered!");

        Quantum.BUS.post(new QuantumLoadingFinishedEvent());
    }
}
