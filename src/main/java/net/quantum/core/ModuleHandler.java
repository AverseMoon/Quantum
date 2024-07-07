package net.quantum.core;

import net.quantum.Quantum;
import net.quantum.core.events.QuantumModuleLoadedEvent;

import java.util.HashMap;
import java.util.Map;

public class ModuleHandler {
    /**
     * The hash map of all loaded modules <br/>
     * {@link Map}<{@link String} name, {@link QuantumModule}module>
     */
    public Map<String, QuantumModule> modules = new HashMap<>();

    /**
     * Used by {@link ModuleLoader}, it is not recommended you use this.
     * @param module the module to add.
     * @return {@code module.name}
     */
    public String addModule(QuantumModule module) {
        if (modules.containsKey(module.name)) throw new RuntimeException("Two modules ('%s' and '%s') are attempting to load with the same name.".formatted(module.jarpath, modules.get(module.name).jarpath));

        modules.put(module.name, module);
        Quantum.BUS.post(new QuantumModuleLoadedEvent(module.name, module));

        return module.name;
    }
}
