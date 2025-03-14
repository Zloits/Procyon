package me.zloits.procyon;

import me.zloits.procyon.logging.ProcyonLogger;
import me.zloits.procyon.util.LogUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

public class CredentialPrintTest {

    @Test
    void printTest() {
        Procyon procyon = new Procyon();

        Logger logger = new ProcyonLogger<>(CredentialPrintTest.class).getLogger();

        System.out.println(
                LogUtil.formatCredentials(
                        "Name", "Izhar",
                        "Age", 15,
                        "City", "Pontianak",
                        "Maximum Users Capacity Size", 200
                )
        );
    }
}
