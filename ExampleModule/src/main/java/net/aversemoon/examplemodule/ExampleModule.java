package net.aversemoon.examplemodule;

import net.quantum.Quantum;
import net.quantum.core.QuantumModule;
import net.quantum.core.events.QuantumModuleLoadedEvent;
import net.simpleeventbus.Subscribe;

public class ExampleModule extends QuantumModule {
    public ExampleModule() {
        Quantum.BUS.register(this);
        name = "example";
    }

    @Subscribe
    public void exampleEventReciever(QuantumModuleLoadedEvent evt) {
        LOGGER.info("Recieved QuantumModuleLoadedEvent, name: %s".formatted(evt.name));
    }
}
