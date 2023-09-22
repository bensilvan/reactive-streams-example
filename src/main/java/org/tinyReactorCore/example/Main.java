package org.tinyReactorCore.example;

import org.tinyReactorCore.example.Impl.publishers.MyFlux;
import org.tinyReactorCore.example.Impl.publishers.MyMono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

        MyFlux.just(List.of(5,10,15))
                .flatMap(x -> {
                    System.out.println("got into flatMap with: " + x);
                    return MyMono.fromFuture(delayAndGetValue(Duration.ofSeconds(x), x, scheduler));
                },2,2).subscribe(x -> System.out.println("finish: " + x + " on thread: " + Thread.currentThread()));

    }

    private static CompletableFuture<Integer> delayAndGetValue(Duration delayInSecond, Integer value, ScheduledExecutorService schedul) {
        var future = new CompletableFuture<Integer>();
        schedul.schedule(() -> future.complete(value), delayInSecond.toSeconds(), TimeUnit.SECONDS);

        return future;
    }
}
