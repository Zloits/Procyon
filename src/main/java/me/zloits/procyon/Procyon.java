package me.zloits.procyon;

import lombok.Getter;
import me.zloits.procyon.connection.ProcyonConnection;
import me.zloits.procyon.logging.ProcyonLogger;
import org.slf4j.Logger;

@Getter
public class Procyon {

    @Getter
    private static Procyon procyon;
    private final ProcyonLogger<Procyon> procyonProcyonLogger = new ProcyonLogger<>(Procyon.class);
    private final Logger logger = procyonProcyonLogger.getLogger();
    private final ProcyonConnection procyonConnection = new ProcyonConnection();

    public Procyon() {
        procyon = this;
    }

}
