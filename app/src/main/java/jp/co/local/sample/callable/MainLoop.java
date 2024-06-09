package jp.co.local.sample.callable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MainLoop {

    public void run() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        List<WatherApiCallable> tasks = new ArrayList<>();
        List<Future<JsonObject>> futures = new ArrayList<>();

        // CyclicBarrier finalLogic = new CyclicBarrier(2, () -> {
        // System.out.println("it's all done.");
        // });

        try {
            tasks.add(new WatherApiCallable(35.6895, 139.6917, "weather_code"));
            tasks.add(new WatherApiCallable(34.6937, 135.5022, "weather_code"));

            for (Callable<JsonObject> task : tasks) {
                futures.add(executor.submit(task));
            }
        } catch (Throwable th) {
            th.printStackTrace();
            return;
        }

        try {
            JsonArray ctx = new JsonArray();
            try {
                for (int i = 0; i < futures.size(); i++) {
                    Future<JsonObject> future = futures.get(i);
                    WatherApiCallable task = tasks.get(i);
                    if (i >= 1) {
                        task.setContext(ctx);
                    }
                    ctx.add(future.get());
                }
                System.out.println("----------------------------");
                System.out.println("result: " + ctx);
            } catch (CancellationException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } finally {
            executor.shutdown();
        }
    }

}
