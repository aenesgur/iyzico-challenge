package com.iyzico.challenge.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@EnableAutoConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAsync
public class IyzicoPaymentServiceTest {

    @Autowired
    private PaymentServiceClients paymentServiceClients;

    /*
        The ThreadPoolConfiguration file I created under the
        Configuration package was customized using the ThreadPoolTaskExecutor class.
        By setting the CorePoolSize value to 2,
        2 threads are running in parallel,
        and the db pool size was set to 2 by default,
        without causing an error in our database,
        and a successful result was obtained despite a waiting time of 5 seconds.
     */
    @Test
    public void should_pay_with_iyzico_with_100_clients_together() {
        List<CompletableFuture> futures = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            CompletableFuture<String> future = paymentServiceClients.call(new BigDecimal(i));
            futures.add(future);
        }
        futures.stream().forEach(f -> CompletableFuture.allOf(f).join());
    }
}
