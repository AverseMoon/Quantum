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

public class Quantum {
    public static final EventBus<Event> BUS = EventBus.createDefault();
    public static final Logger LOGGER = LogManager.getLogger(Quantum.class);
    public static final ModuleHandler MODULE_HANDLER = new ModuleHandler();

    public static final Path MODULES_PATH = Path.of("./modules");
    public static final Path CONFIG_PATH = Path.of("./config");

    public static void main(String[] args) throws ReflectiveOperationException, IOException {
        LOGGER.info("Checking the folder '%s' exists...".formatted(MODULES_PATH.toString()));
        if (!Files.exists(MODULES_PATH)) Files.createDirectory(MODULES_PATH);
        if (!Files.isDirectory(MODULES_PATH)) throw new RuntimeException("'%s' must be a directory".formatted(MODULES_PATH.toString()));

        LOGGER.info("Registering events...");
        BUS.registerEventType(QuantumModuleLoadedEvent.class);
        BUS.registerEventType(QuantumLoadingFinishedEvent.class);

        ModuleLoader.registerAllModulesInDirectory(MODULES_PATH, MODULE_HANDLER);
    }
}
