package me.zloits.procyon;

import lombok.Getter;
import lombok.SneakyThrows;
import me.zloits.procyon.connection.ProcyonConnection;
import me.zloits.procyon.logging.ProcyonLogger;
import me.zloits.procyon.redis.ProcyonRedisConnection;
import me.zloits.procyon.sql.SQLConnection;
import me.zloits.procyon.sql.query.QueryExecutor;
import me.zloits.procyon.sql.query.QueryGetter;

import java.util.List;
import java.util.UUID;

public class ProcyonTest {

    public static void main(String[] args) {
        testC();
        testA();
        testB();
        testD();
        // testE();
        testF();
        testG();
    }

    public static void testC() {
        Procyon procyon = new Procyon();
        procyon.getProcyonProcyonLogger().getLogger().info("Test C: WORKS");
    }

    public static void testA() {
        ProcyonLogger procyonLogger = new ProcyonLogger<>(ProcyonTest.class);
        procyonLogger.getLogger().info("Test A: WORKS");
    }

    @Getter
    private final static ProcyonConnection procyonConnection = new ProcyonConnection();

    @SneakyThrows
    public static void testB() {
        SQLConnection sqlConnection = SQLConnection.createConnection(
                "192.168.x.x", 3306, "procyon", "root", "root", false
        );

        procyonConnection.getConnections().add(sqlConnection);
        System.out.println("Test B: WORKS");
    }

    @SneakyThrows
    public static void testD() {
        SQLConnection sqlConnection = (SQLConnection) procyonConnection.getConnection("procyon");

        new QueryExecutor(sqlConnection, "create table if not exists procyon(id varchar(255))", () -> {
            System.out.println("Test D: WORKS");
        }, List.of()).start();
    }

    private static final UUID uuid = UUID.randomUUID();

    @SneakyThrows
    public static void testE() {
        SQLConnection sqlConnection = (SQLConnection) procyonConnection.getConnection("procyon");

        new QueryExecutor(sqlConnection, "insert into procyon (id) values (?)", () -> {
            System.out.println("Test E: WORKS");
        }, List.of(uuid.toString())).start();
    }

    @SneakyThrows
    public static void testF() {
        SQLConnection sqlConnection = (SQLConnection) procyonConnection.getConnection("procyon");

        QueryGetter<UUID> queryGetter = new QueryGetter<>(sqlConnection, "select id from procyon where id = ?", resultSet -> {
            while (resultSet.next()) {
                System.out.println("Test F: WORKS (1)");
            }

            return uuid;
        }, List.of(uuid.toString())).start();

        if (queryGetter.isPresent()) {
            System.out.println("Test F: WORKS (2)");

            queryGetter.get();
        }
    }

    public static void testG() {
        ProcyonRedisConnection redisConnection = ProcyonRedisConnection.createConnection("192.168.1.8", 6379);
        procyonConnection.getConnections().add(redisConnection);
        System.out.println("Test G: WORKS");
    }
}
