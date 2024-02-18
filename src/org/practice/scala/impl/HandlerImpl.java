package org.practice.scala.impl;

import org.practice.scala.domain.Client;
import org.practice.scala.domain.Event;
import org.practice.scala.domain.Handler;
import org.practice.scala.domain.Result;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.practice.scala.domain.Result.REJECTED;

public class HandlerImpl implements Handler {
    private static final long RETRY_TIMEOUT_SECONDS = 5;

    // Не сказано в задании сколько повторных попыток
    // -1 для бесконечного количества и положительное для фиксированного
    private static final int RETRY_TIMES = 3;


    private ExecutorService executor;

    private Client client;

    public HandlerImpl(Client client, ExecutorService executor) {
        this.client = client;
        this.executor = executor;
    }

    @Override
    public Duration timeout() {
        return Duration.ofSeconds(RETRY_TIMEOUT_SECONDS);
    }

    @Override
    public void performOperation() {
        Event chunk = client.readData();
        chunk.recipients().forEach(consumer -> {
            Runnable sendTask = () -> {
                Result res = client.sendData(consumer, chunk.payload());
                int retries = RETRY_TIMES;
                while (res == REJECTED && (RETRY_TIMES == -1 || retries > 0)) {
                    try {
                        retries--;
                        TimeUnit.MILLISECONDS.sleep(timeout().toMillis()); // Это сильно напрягает. Но первый вариант - так.
                        res = client.sendData(consumer, chunk.payload());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            executor.submit(sendTask);
        });

    }
}
