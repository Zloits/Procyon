package me.zloits.procyon.util.executor;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.ThreadFactory;

@AllArgsConstructor
@Data
public class PoolConfiguration {

    private String poolName;
    private ThreadFactory threadFactory;
    private int nThreads;
}
