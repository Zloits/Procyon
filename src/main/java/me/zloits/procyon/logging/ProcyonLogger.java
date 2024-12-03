package me.zloits.procyon.logging;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class ProcyonLogger<T> {

    private final Logger logger;
    private final Class<T> clazz;

    public ProcyonLogger(Class<T> clazz) {
        this.clazz = clazz;

        logger = LoggerFactory.getLogger(clazz);
    }
}
