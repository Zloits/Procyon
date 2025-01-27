package me.zloits.procyon;

import me.zloits.procyon.http.ProcyonHttpAPI;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.jupiter.api.Test;

public class ProcyonHttpTest {

    private final Procyon procyon = new Procyon();
    private final ProcyonHttpAPI procyonHttpAPI = new ProcyonHttpAPI(HttpClients.createDefault());

    @Test
    void postTest() {
        procyonHttpAPI.setBASE_URL("https://luckynetwork.net/");
        procyonHttpAPI.POST("", "");
        System.out.println("Posted");
    }
}
