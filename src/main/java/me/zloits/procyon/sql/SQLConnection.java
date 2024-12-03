package me.zloits.procyon.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.*;
import me.zloits.procyon.Procyon;
import me.zloits.procyon.connection.IConnection;

import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
@Value
public class SQLConnection implements IConnection {

    private static Procyon procyon = Procyon.getProcyon();

    @NonNull
    String database;
    @NonNull
    Connection connection;

    public static SQLConnection createConnection(@NonNull String host, int port, @NonNull String database, @NonNull String username, @NonNull String password, boolean ssl) throws SQLException {
        procyon.getLogger().info("Connecting to {}:{}/{} use SSL = {}.", host, port, database, ssl);

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?allowPublicKeyRetrieval=true&useSSL=" + ssl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        procyon.getLogger().info("Connection {}:{}/{} use SSL = {} is connected!", host, port, database, ssl);

        return new SQLConnection(database, hikariDataSource.getConnection());
    }

    @Override
    public String getPrimaryName() {
        return database;
    }
}
