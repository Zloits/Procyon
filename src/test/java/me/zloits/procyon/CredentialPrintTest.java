package me.zloits.procyon;

import me.zloits.procyon.logging.ProcyonLogger;
import me.zloits.procyon.sql.SQLConnection;
import me.zloits.procyon.sql.query.QueryGetter;
import me.zloits.procyon.util.LogUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.List;

public class CredentialPrintTest {

//    @Test
//    void printTest() {
//        Procyon procyon = new Procyon();
//
//        Logger logger = new ProcyonLogger<>(CredentialPrintTest.class).getLogger();
//
//        System.out.println(
//                LogUtil.formatCredentials(
//                        "Name", "Izhar",
//                        "Age", 15,
//                        "City", "Pontianak",
//                        "Maximum Users Capacity Size", 200
//                )
//        );
//    }
//
//    @Test
//    void sqlTest() {
//        try {
//            SQLConnection sqlConnection = SQLConnection.createConnection("0.0.0.0", 3306, "test", "root", "", false);
//
//            QueryGetter<ExampleInstance> queryGetter = new QueryGetter<>(sqlConnection, "select * from instances", resultSet -> {
//                return new ExampleInstance();
//            }, List.of());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
}
