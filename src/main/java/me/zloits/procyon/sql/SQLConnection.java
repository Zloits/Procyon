package me.zloits.procyon.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.*;
import me.zloits.procyon.Procyon;
import me.zloits.procyon.connection.IConnection;
import me.zloits.procyon.util.InstanceGetter;
import me.zloits.procyon.util.LogUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * {@link SQLConnection} represents a connection to an SQL database.
 * It utilizes HikariCP for connection pooling and provides a structured way
 * to establish and manage database connections.
 */
@RequiredArgsConstructor
@Value
public class SQLConnection implements IConnection {

    private static Procyon procyon = InstanceGetter.get(Procyon.class);

    @NonNull
    String database;
    @NonNull
    Connection connection;

    /**
     * Establishes a new SQL connection using HikariCP.
     *
     * @param host     The database server host.
     * @param port     The database server port.
     * @param database The name of the database.
     * @param username The database username.
     * @param password The database password.
     * @param ssl      Whether SSL should be used for the connection.
     * @return A new {@link SQLConnection} instance.
     * @throws SQLException If an error occurs while establishing the connection.
     */
    public static SQLConnection createConnection(@NonNull String host, int port, @NonNull String database,
                                                 @NonNull String username, @NonNull String password, boolean ssl)
            throws SQLException {
        System.out.println(LogUtil.formatCredentials(
                "Host", host,
                "Port", port,
                "Database", database,
                "SSL", ssl
        ));
        procyon.getLogger().info("Connecting to SQL Connection...");

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?allowPublicKeyRetrieval=true&useSSL=" + ssl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        procyon.getLogger().info("Connection {}:{}/{} use SSL = {} is connected!", host, port, database, ssl);

        return new SQLConnection(database, hikariDataSource.getConnection());
    }

    /**
     * Retrieves the primary name of the connection, which is the database name.
     *
     * @return The database name.
     */
    @Override
    public String getPrimaryName() {
        return database;
    }

    @Override
    public boolean isConnected() {
        try {
            return connection.isValid(2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}