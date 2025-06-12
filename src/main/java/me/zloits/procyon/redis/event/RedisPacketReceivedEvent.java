package me.zloits.procyon.redis.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.zloits.procyon.event.Event;
import me.zloits.procyon.redis.RedisPacket;

@AllArgsConstructor
@Getter
public class RedisPacketReceivedEvent extends Event {

    private final RedisPacket redisPacket;
}
