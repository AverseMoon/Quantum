package net.quantum;

import net.simpleeventbus.EventBus;
import net.simpleeventbus.event.Event;
import net.quantum.core.ModuleHandler;
import net.quantum.core.ModuleLoader;
import net.quantum.core.events.QuantumLoadingFinishedEvent;
import net.quantum.core.events.QuantumModuleLoadedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Quantum {
    public static final EventBus<Event> BUS = EventBus.createDefault();
    public static final Logger LOGGER = LogManager.getLogger(Quantum.class);
    public static final ModuleHandler MODULE_HANDLER = new ModuleHandler();

    public static final Path MODULES_PATH = Path.of("./modules");
    public static final Path CONFIG_PATH = Path.of("./config");

    public static final String VERSION = "quantum-v1.1";

    public static void main(String[] args) throws ReflectiveOperationException, IOException {
        List<String> argsList = Arrays.asList(args);

        if (argsList.contains("-help") || argsList.contains("-?") || argsList.contains("help") || argsList.contains("?")) {
            System.out.println("List of all arguments:");
            System.out.println("  -help / help / -? / ? : Exits with this text.");
            System.out.println("  -warnMissing : When a module is missing a dependency, give a warning instead of throwing an error.");
            System.out.println("  -warnDuplicates : When there are two modules with the same global name, give a warning instead of throwing an error.");

            return;
        }

        LOGGER.info("Checking the folder '%s' exists...".formatted(MODULES_PATH.toString()));
        if (!Files.exists(MODULES_PATH)) Files.createDirectory(MODULES_PATH);
        if (!Files.isDirectory(MODULES_PATH)) throw new RuntimeException("'%s' must be a directory".formatted(MODULES_PATH.toString()));
        LOGGER.info("DONE");

        LOGGER.info("Checking the folder '%s' exists...".formatted(CONFIG_PATH.toString()));
        if (!Files.exists(CONFIG_PATH)) Files.createDirectory(CONFIG_PATH);
        if (!Files.isDirectory(CONFIG_PATH)) throw new RuntimeException("'%s' must be a directory".formatted(CONFIG_PATH.toString()));
        LOGGER.info("DONE");

        LOGGER.info("Registering events...");
        BUS.registerEventType(QuantumModuleLoadedEvent.class);
        BUS.registerEventType(QuantumLoadingFinishedEvent.class);
        LOGGER.info("DONE");

        ModuleLoader.registerAllModulesInDirectory(MODULES_PATH, MODULE_HANDLER, !argsList.contains("-warnMissing"), !argsList.contains("-warnDuplicates"));
    }
}
