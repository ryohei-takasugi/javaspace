package jp.co.local.sample.loop;

import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MainLoop {
    public void run(String[] args) {

        if (args == null || args.length != 1)
            throw new IllegalArgumentException("args");

        Path jsonConfigFilePath = Path.of(args[0]);

        ExecutorService singleThread = Executors.newSingleThreadExecutor();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        HttpClient client = HttpClient.newBuilder().executor(singleThread).build();

        CompletableFuture<Config> configFut = Config.load(jsonConfigFilePath, singleThread);
        configFut.thenAcceptAsync(config -> {
            System.out.println("config: " + config.toJSON());

            AsyncHttpGetTaskOptions options = new AsyncHttpGetTaskOptions.Builder()
                    .setTaskName("task1")
                    .setUrl(config.getUrl1())
                    .setClient(client)
                    .setContext(new JsonObject())
                    .build();
            AsyncHttpGetTask task = AsyncHttpGetTask.create(options);

            // 10秒周期で1〜3を繰り返し実行する
            scheduler.scheduleWithFixedDelay(() -> {
                try {
                    fetch(task, new JsonArray(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 0, 10, TimeUnit.SECONDS);

        }, singleThread).exceptionally(th -> {
            th.printStackTrace();
            return null;
        });
    }

    private void fetch(AsyncHttpGetTask task, JsonArray collectedData, int count) {
        task.execute().thenAccept(response -> {
            if (response != null && count < 3) {
                collectedData.add((JsonObject) response);
                fetch(task, collectedData, count + 1);
            } else {
                printActiveThreads();
                writeToFile(collectedData);
            }
        }).exceptionally(th -> {
            th.printStackTrace();
            return null;
        });
    }

    private void writeToFile(JsonArray data) {
        try {
            Path outputPath = Paths.get("output.json");
            Files.writeString(outputPath, data.toString(), StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Written data to file: " + data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 現在アクティブなスレッドの情報を出力します。
     */
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
