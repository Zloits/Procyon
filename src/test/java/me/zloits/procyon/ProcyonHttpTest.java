package me.zloits.procyon;

import me.zloits.procyon.http.v2.ProcyonHttpAPI;
import me.zloits.procyon.http.v1.ProcyonHttpAPIV1;
import me.zloits.procyon.http.v2.ProcyonHttpAPIV2;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.jupiter.api.Test;

public class ProcyonHttpTest {

    private final Procyon procyon = new Procyon();
    private final ProcyonHttpAPIV1 procyonHttpAPIV1 = new ProcyonHttpAPIV1(HttpClients.createDefault());

    @Test
    void postTest() {
        procyonHttpAPIV1.setBASE_URL("https://luckynetwork.net/");
        procyonHttpAPIV1.POST("", "");
        System.out.println("Posted");
    }

    private final ProcyonHttpAPI procyonHttpAPI = ProcyonHttpAPIV2.builder().build();
}
