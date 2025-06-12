package me.zloits.procyon.connection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class ProcyonConnections {

    /**
     * A thread-safe map storing all registered connections.
     */
    private final Map<String, IConnection> connections = new ConcurrentHashMap<>();

    /**
     * Retrieves a connection by its primary name.
     *
     * @param primaryName The unique identifier of the connection.
     * @return An {@link Optional} containing the connection if found, otherwise empty.
     */
    public Optional<IConnection> getConnection(@NonNull String primaryName) {
        return Optional.ofNullable(connections.get(primaryName));
    }

    /**
     * Registers a new connection if it's not already registered.
     *
     * @param connection The connection to add.
     * @return {@code true} if added successfully, {@code false} if it already exists.
     */
    public boolean registerConnection(@NonNull IConnection connection) {
        return connections.putIfAbsent(connection.getPrimaryName(), connection) == null;
    }

    /**
     * Unregisters a connection by its primary name.
     *
     * @param primaryName The unique identifier of the connection.
     * @return {@code true} if successfully removed, otherwise {@code false}.
     */
    public boolean unregisterConnection(@NonNull String primaryName) {
        return connections.remove(primaryName) != null;
    }

    /**
     * Retrieves list of active connections.
     *
     * @return An {@link List} containing the active connection.
     */
    public List<IConnection> getActiveConnections() {
        return connections.values().stream()
                .filter(IConnection::isConnected)
                .collect(Collectors.toList());
    }
}

