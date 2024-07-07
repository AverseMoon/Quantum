package net.quantum.core;

import net.quantum.Quantum;
import net.simpleeventbus.EventBus;
import net.simpleeventbus.event.Event;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public abstract class QuantumModule {
    /**
     * The jar path relative to {@link Quantum#MODULES_PATH}. <br/>
     * It is not recommended to set this as it is used internally for debugging.
     */
    public Path jarpath;
    /**
     * It is recommended to keep your event bus separate from {@link Quantum#BUS} (if you use a separate event bus)
     */
    public EventBus<Event> bus = null;
    /**
     * Optional configuration file
     * <br/><br/>
     * To register the configuration, just initialize the file with {@link ConfigurationFile#ConfigurationFile(String)} and then call {@link ConfigurationFile#loadAndCheck()}.
     */
    public ConfigurationFile configuration = null;
    /**
     * The instance-specific name of the module, set this in {@link QuantumModule#QuantumModule()}.
     */
    public String name;

    /**
     * The global name of the module, set this in the manifest attribute {@code Module-Global-Name} of your jar.
     * Used for dependency checking.
     */
    public String globalName;

    /**
     * A list of global module names which this module depends on, if one is not found it will crash, set this in the manifest attribute {@code Module-Dependencies} as a JSON array.
     */
    public List<String> dependencies;

    /**
     * Initialization <br/>
     * NOTE: This function is different from {@link QuantumModule#register()}, this is fired before all the modules have been completely loaded, it is recommended you register {@code this} to {@link Quantum#BUS} in this function.
     */
    public QuantumModule() {
        name = UUID.randomUUID().toString();
    }

    /**
     * Initialization <br/>
     * This function is called AFTER all modules have been loaded.
     */
    public void register() {

    }
}
