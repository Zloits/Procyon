package me.zloits.procyon.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.*;
import me.zloits.procyon.connection.IConnection;
import me.zloits.procyon.logging.ProcyonLogger;
import me.zloits.procyon.util.LogUtil;
import me.zloits.procyon.util.executor.ExecutorType;
import me.zloits.procyon.util.executor.ExecutorUtil;
import me.zloits.procyon.util.executor.PoolConfiguration;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * {@link SQLConnection} represents a connection to an SQL database.
 * It utilizes HikariCP for connection pooling and provides a structured way
 * to establish and manage database connections.
 */
public record SQLConnection(ExecutorService executorService, @NonNull String database,
                            @NonNull Connection connection) implements IConnection {

    static Logger logger = new ProcyonLogger<>(SQLConnection.class).getLogger();

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
        logger.info("Connecting to SQL Connection...");

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?allowPublicKeyRetrieval=true&useSSL=" + ssl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        logger.info("Connection {}:{}/{} use SSL = {} is connected!", host, port, database, ssl);

        ExecutorService executorService1 = ExecutorUtil.createPool(
                new PoolConfiguration("mysql-" + host, Executors.defaultThreadFactory(), 2),
                ExecutorType.FIXED
        );

        return new SQLConnection(executorService1, database, hikariDataSource.getConnection());
    }

    public Logger getLogger() {
        return logger;
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