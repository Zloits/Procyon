package me.zloits.procyon.exception;

import me.zloits.procyon.redis.RedisPacket;

public class PacketTimeoutException extends ProcyonException {

    public PacketTimeoutException(RedisPacket redisPacket, String message) {
        super("Packet: " + redisPacket.getChannel() + " timeout: " + message);
    }
}
