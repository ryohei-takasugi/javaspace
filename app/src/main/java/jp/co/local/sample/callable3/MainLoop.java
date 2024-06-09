package jp.co.local.sample.callable3;

import java.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.JsonObject;

/**
 * MainLoopクラスは、複数の非同期タスクをシリアルに実行するメインループを定義します。
 */
public class MainLoop {

    /**
     * メインループを実行するメソッド。
     * 複数の非同期タスクをシリアルに実行し、最終結果を処理します。
     */
    public void run() {
        System.out.println("----------------------------");
        System.out.println("Thread Number: " + Thread.currentThread().getId());
        System.out.println("Thread Count1: " + Thread.activeCount());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        HttpClient client = HttpClient.newBuilder().executor(executor).build();

        AsyncHttpGetTask firstTask = new AsyncHttpGetTask(
                "https://api.open-meteo.com/v1/forecast?latitude=35.6895&longitude=139.6917&current=weather_code",
                client);
        AsyncHttpGetTask task2 = new AsyncHttpGetTask(
                "https://api.open-meteo.com/v1/forecast?latitude=34.6937&longitude=135.5022&current=weather_code",
                client);
        AsyncHttpGetTask task3 = new AsyncHttpGetTask(
                "https://api.open-meteo.com/v1/forecast?latitude=43.0667&longitude=141.35&current=weather_code",
                client);

        // 最初のタスクを非同期に実行し、結果をハンドリング
        CompletableFuture<AsyncResult<JsonObject>> future = firstTask.get();

        // 2番目のタスクをシリアルに実行
        future = future.thenComposeAsync(ar -> {
            if (ar.failed()) {
                System.out.println("tt");
                return CompletableFuture.failedFuture(ar.cause());
            }
            task2.setContext(ar.result());
            return task2.get();
        }, executor);

        // 3番目のタスクをシリアルに実行
        future = future.thenComposeAsync(ar -> {
            if (ar.failed()) {
                System.out.println("bbb");
                return CompletableFuture.failedFuture(ar.cause());
            }
            task3.setContext(ar.result());
            return task3.get();
        }, executor);

        future = future.exceptionally(th -> {
            System.out.println("kkk");
            th.printStackTrace();
            return new AsyncResult<JsonObject>(new JsonObject(), th);
        });

        // 全てのタスクが完了した後の処理を定義
        future.thenAcceptAsync(ar -> {
            if (ar.failed()) {
                System.out.println("aaaa");
                ar.cause().printStackTrace();
            } else {
                System.out.println(ar.result());
            }
            System.out.println("Thread Count4: " + Thread.activeCount());
        }, executor).thenRun(() -> {
            try {
                System.out.println("ccc");
                executor.shutdown();
                if (!executor.awaitTermination(800, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
            System.out.println("ExecutorService shut down");

            // スレッドの詳細を出力
            printActiveThreads();
        });

        // 非同期処理の完了を待つ
        future.join();
    }

    private void printActiveThreads() {
        Thread[] threads = new Thread[Thread.activeCount()];
        Thread.enumerate(threads);
        for (Thread t : threads) {
            if (t != null) {
                System.out.println(t.getName() + " - " + t.getState());
            }
        }
    }
}
