package me.zloits.procyon.util.executor;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.ThreadFactory;

@AllArgsConstructor
@Data
public class SimplePoolConfiguration {

    private String poolName;
    private ThreadFactory threadFactory;
    private int nThreads = 1;
}
