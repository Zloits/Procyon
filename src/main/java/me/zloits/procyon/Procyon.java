package me.zloits.procyon;

import lombok.Getter;
import me.zloits.procyon.logging.ProcyonLogger;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class Procyon {

    @Getter
    private static Procyon procyon;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final ProcyonLogger<Procyon> procyonProcyonLogger = new ProcyonLogger<>(Procyon.class);
    private final Logger logger = procyonProcyonLogger.getLogger();

    public Procyon() {
        procyon = this;
    }
}
