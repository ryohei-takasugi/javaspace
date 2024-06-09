package jp.co.local.sample.callable4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import com.google.gson.JsonObject;

public class SerialTaskExecutor {

    private final List<Task<JsonObject>> tasks = new ArrayList<>();
    private final ExecutorService executor;
    private final Task<JsonObject> firstTask;

    public static SerialTaskExecutor create(Task<JsonObject> firstTask) {
        return new SerialTaskExecutor(firstTask, Executors.newSingleThreadExecutor());
    }

    public SerialTaskExecutor(Task<JsonObject> firstTask, ExecutorService executor) {
        this.firstTask = firstTask;
        this.executor = executor;
    }

    public void addTask(Task<JsonObject> task) {
        tasks.add(task);
    }

    public void executeTasks(Consumer<AsyncResult<JsonObject>> callback) {
        if (tasks.isEmpty()) {
            throw new IllegalStateException("No tasks to execute");
        }

        CompletableFuture<AsyncResult<JsonObject>> future = firstTask.execute();

        for (Task<JsonObject> task : tasks) {
            future = future.thenComposeAsync(new ComposeHander(task), executor);
        }

        future.thenAcceptAsync(result -> {
            callback.accept(result);
            shutdownExecutor();
        }, executor);

        future.thenRun(this::printActiveThreads);

        future.join();
    }

    private void shutdownExecutor() {
        try {
            executor.shutdown();
            if (!executor.awaitTermination(800, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                System.out.println("WARN! ExecutorService shut down Now");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        System.out.println("ExecutorService shut down");
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
