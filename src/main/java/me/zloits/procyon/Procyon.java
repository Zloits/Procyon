package me.zloits.procyon;

import lombok.Getter;
import me.zloits.procyon.connection.ProcyonConnections;
import me.zloits.procyon.event.EventManager;
import me.zloits.procyon.logging.ProcyonLogger;
import me.zloits.procyon.util.InstanceRegistry;
import org.slf4j.Logger;

@Getter
public class Procyon {

    private final ProcyonLogger<Procyon> procyonProcyonLogger = new ProcyonLogger<>(Procyon.class);
    private final Logger logger = procyonProcyonLogger.getLogger();
    private final ProcyonConnections procyonConnections = new ProcyonConnections();

    private final EventManager eventManager = new EventManager();

    public Procyon() {
        InstanceRegistry.add(this);
    }

}
