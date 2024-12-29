package me.zloits.procyon.redis;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public abstract class RedisPacket {

    private final ProcyonRedisConnection procyonRedisConnection;

    @NonNull
    public abstract String getChannel();
}
