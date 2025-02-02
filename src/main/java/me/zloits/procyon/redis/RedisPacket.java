package me.zloits.procyon.redis;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public abstract class RedisPacket {

    @NonNull
    public abstract String getChannel();
}
