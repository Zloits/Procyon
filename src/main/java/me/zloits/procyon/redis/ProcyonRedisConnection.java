package me.zloits.procyon.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import lombok.NonNull;
import lombok.Value;
import me.zloits.procyon.connection.IConnection;
import me.zloits.procyon.util.GsonUtil;

@Value
public class ProcyonRedisConnection implements IConnection {

    String host;
    RedisClient redisClient;

    StatefulRedisConnection<String, String> statefulRedisConnection;
    RedisCommands<String, String> redisCommands;
    StatefulRedisPubSubConnection<String, String> pubSubConnection;

    public ProcyonRedisConnection(@NonNull String host, @NonNull RedisClient redisClient) {
        this.host = host;
        this.redisClient = redisClient;

        this.statefulRedisConnection = redisClient.connect();
        this.redisCommands = statefulRedisConnection.sync();
        this.pubSubConnection = redisClient.connectPubSub();
    }

    public static ProcyonRedisConnection createConnection(@NonNull String host, int port) {
        RedisClient redisClient = RedisClient.create("redis://" + host + ":" + port);

        return new ProcyonRedisConnection(host, redisClient);
    }

    public void publishMessage(@NonNull RedisPacket redisPacket, ResponseCallback responseCallback) {
        pubSubConnection.addListener(new RedisPubSubListener<String, String>() {

            @Override
            public void message(String s, String s2) {
                String channel = redisPacket.getChannel() + "Response";

                if (s.equals(channel)) {
                    responseCallback.call(s2);
                }
            }

            @Override
            public void message(String s, String k1, String s2) {

            }

            @Override
            public void subscribed(String s, long l) {

            }

            @Override
            public void psubscribed(String s, long l) {

            }

            @Override
            public void unsubscribed(String s, long l) {

            }

            @Override
            public void punsubscribed(String s, long l) {

            }
        });

        pubSubConnection.sync().subscribe(redisPacket.getChannel() + "Response");
        redisCommands.publish(redisPacket.getChannel(), GsonUtil.toJson(redisPacket));

    }

    @Override
    public String getPrimaryName() {
        return host;
    }
}
