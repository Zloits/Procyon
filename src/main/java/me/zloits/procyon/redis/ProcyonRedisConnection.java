package me.zloits.procyon.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import lombok.NonNull;
import lombok.Value;
import me.zloits.procyon.connection.IConnection;
import me.zloits.procyon.exception.PacketTimeoutException;
import me.zloits.procyon.exception.ProcyonException;
import me.zloits.procyon.util.ExecutorUtil;
import me.zloits.procyon.util.GsonUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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

    public void publishPacket(@NonNull RedisPacket redisPacket, ResponseCallback responseCallback, long timeout) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        ExecutorUtil.getScheduledExecutor().schedule(() -> {
            if (!completableFuture.isDone()) {
                throw new PacketTimeoutException(redisPacket, "Timeout reached " + timeout + " without received any response.");
            }
        }, timeout, TimeUnit.MILLISECONDS);

        pubSubConnection.addListener(new RedisPubSubListener<String, String>() {

            @Override
            public void message(String s, String s2) {
                String channel = redisPacket.getChannel() + "Response";

                if (s.equals(channel)) {
                    responseCallback.call(s2);
                    completableFuture.complete(null);
                    pubSubConnection.sync().unsubscribe(redisPacket.getChannel() + "Response");
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

        try {
            completableFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new ProcyonException(e.getMessage());
        } finally {
            pubSubConnection.sync().unsubscribe(redisPacket.getChannel() + "Response");
        }
    }

    @Override
    public String getPrimaryName() {
        return host;
    }
}
