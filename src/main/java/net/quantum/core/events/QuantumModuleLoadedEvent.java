package net.quantum.core.events;

import net.quantum.core.QuantumModule;
import net.simpleeventbus.event.Event;

public class QuantumModuleLoadedEvent extends Event {
    public String name;
    public QuantumModule module;

    public QuantumModuleLoadedEvent(String name, QuantumModule module) {
        this.name = name;
        this.module = module;
    }
}