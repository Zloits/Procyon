package me.zloits.procyon.redis;

import io.lettuce.core.RedisClient;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import me.zloits.procyon.connection.IConnection;

@RequiredArgsConstructor
@Value
public class ProcyonRedisConnection implements IConnection {

    String host;
    RedisClient redisClient;

    public static ProcyonRedisConnection createConnection(@NonNull String host, int port) {
        RedisClient redisClient = RedisClient.create("redis://" + host + ":" + port);

        return new ProcyonRedisConnection(host, redisClient);
    }

    @Override
    public String getPrimaryName() {
        return host;
    }
}
