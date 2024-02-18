package org.practice.scala;

import org.practice.scala.domain.Handler;
import org.practice.scala.impl.ClientImpl;
import org.practice.scala.impl.HandlerImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main {
    public static void main(String[] args) {
        // Старая привычка из Gentoo-шных CXX_FLAGS. Но можно масштабировать
        int capacity = Runtime.getRuntime().availableProcessors() * 2 + 1;
        // Фиксированный размер - чтобы складывались задачи в очередь, если будет "зашиваться", а не порождались потоки
        // По-идее, при перегрузке так позже упадём с Out of memory
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(capacity);
        HandlerImpl handler = new HandlerImpl(new ClientImpl(), executor);

    }
}