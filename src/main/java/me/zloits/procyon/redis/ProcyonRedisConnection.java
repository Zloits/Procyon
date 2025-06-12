package me.zloits.procyon.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import lombok.NonNull;
import lombok.Value;
import me.zloits.procyon.Procyon;
import me.zloits.procyon.connection.IConnection;
import me.zloits.procyon.exception.PacketTimeoutException;
import me.zloits.procyon.exception.ProcyonException;
import me.zloits.procyon.redis.event.RedisPacketReceivedEvent;
import me.zloits.procyon.util.InstanceRegistry;
import me.zloits.procyon.util.executor.ExecutorUtil;
import me.zloits.procyon.util.GsonUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Represents a Redis connection wrapper for Procyon, providing synchronous
 * and Pub/Sub functionality.
 */
@Value
public class ProcyonRedisConnection implements IConnection {

    /** The Redis host address. */
    String host;

    /** The Redis client instance. */
    RedisClient redisClient;

    /** A stateful Redis connection for synchronous commands. */
    StatefulRedisConnection<String, String> statefulRedisConnection;

    /** Redis command interface for executing commands synchronously. */
    RedisCommands<String, String> redisCommands;

    /** A stateful Pub/Sub connection for handling message subscriptions. */
    StatefulRedisPubSubConnection<String, String> pubSubConnection;

    /**
     * Constructs a new Redis connection instance.
     *
     * @param host The Redis server host.
     * @param redisClient The Redis client instance.
     */
    private ProcyonRedisConnection(@NonNull String host, @NonNull RedisClient redisClient) {
        this.host = host;
        this.redisClient = redisClient;
        this.statefulRedisConnection = redisClient.connect();
        this.redisCommands = statefulRedisConnection.sync();
        this.pubSubConnection = redisClient.connectPubSub();

        // Listen for the response on the corresponding channel
        pubSubConnection.addListener(new RedisPubSubListener<String, String>() {
            @Override
            public void message(String channel, String message) {
                RedisPacket redisPacket = GsonUtil.fromJson(message, RedisPacket.class);

                String responseChannel = redisPacket.getChannel();
                if (channel.equals(responseChannel)) {
                    RedisPacketReceivedEvent redisPacketReceivedEvent = new RedisPacketReceivedEvent(redisPacket);
                    InstanceRegistry.get(Procyon.class).orElseThrow().getEventManager().callEvent(redisPacketReceivedEvent);
                }
            }

            @Override public void message(String s, String k1, String s2) {}
            @Override public void subscribed(String s, long l) {}
            @Override public void psubscribed(String s, long l) {}
            @Override public void unsubscribed(String s, long l) {}
            @Override public void punsubscribed(String s, long l) {}
        });
    }

    /**
     * Creates a new Redis connection to the specified host and port.
     *
     * @param host The Redis server host.
     * @param port The Redis server port.
     * @return A new instance of {@code ProcyonRedisConnection}.
     */
    public static ProcyonRedisConnection createConnection(@NonNull String host, int port) {
        RedisClient redisClient = RedisClient.create("redis://" + host + ":" + port);
        return new ProcyonRedisConnection(host, redisClient);
    }

    /**
     * Publishes a packet to a Redis channel and waits for a response within the given timeout.
     *
     * @param redisPacket The packet to publish.
     * @param responseCallback The callback to execute when a response is received.
     * @param timeout The maximum wait time (in milliseconds) for a response.
     */
    public void publishPacket(@NonNull RedisPacket redisPacket, ResponseCallback responseCallback, long timeout) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        // Schedule a timeout task
        ExecutorUtil.getScheduledExecutor().schedule(() -> {
            if (!completableFuture.isDone()) {
                throw new PacketTimeoutException(redisPacket, "Timeout reached " + timeout + "ms without receiving any response.");
            }
        }, timeout, TimeUnit.MILLISECONDS);

        // Listen for the response on the corresponding channel
        pubSubConnection.addListener(new RedisPubSubListener<String, String>() {
            @Override
            public void message(String channel, String message) {
                String responseChannel = redisPacket.getChannel() + "Response";
                if (channel.equals(responseChannel)) {
                    responseCallback.call(message);
                    completableFuture.complete(null);
                    pubSubConnection.sync().unsubscribe(responseChannel);
                }
            }

            @Override public void message(String s, String k1, String s2) {}
            @Override public void subscribed(String s, long l) {}
            @Override public void psubscribed(String s, long l) {}
            @Override public void unsubscribed(String s, long l) {}
            @Override public void punsubscribed(String s, long l) {}
        });

        pubSubConnection.sync().subscribe(redisPacket.getChannel() + "Response");
        redisCommands.publish(redisPacket.getChannel(), GsonUtil.toJson(redisPacket));

        try {
            completableFuture.get(); // Wait for response
        } catch (ExecutionException | InterruptedException e) {
            throw new ProcyonException(e.getMessage());
        } finally {
            pubSubConnection.sync().unsubscribe(redisPacket.getChannel() + "Response");
        }
    }

    /**
     * Gets the primary name of the connection, which is the host.
     *
     * @return The Redis host address.
     */
    @Override
    public String getPrimaryName() {
        return host;
    }

    @Override
    public boolean isConnected() {
        return redisClient.connect().sync().ping().equalsIgnoreCase("PONG");
    }
}
