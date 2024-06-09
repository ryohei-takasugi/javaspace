package jp.co.local.sample.callable2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.gson.JsonObject;

/**
 * SerialTaskExecutorクラスは、複数のタスクをシリアルに実行するためのクラスです。
 * 各タスクは非同期に実行され、前のタスクの結果が次のタスクに渡されます。
 */
public class SerialTaskExecutor {

    private final Task firstTask; // 最初に実行するタスク
    private final ExecutorService executor; // タスクを実行するためのExecutorService

    private List<Task> tasks = new ArrayList<>(); // 後続のタスクを保持するリスト

    /**
     * SerialTaskExecutorインスタンスを作成するファクトリメソッド。
     *
     * @param firstTask 最初に実行するタスク
     * @return SerialTaskExecutorインスタンス
     */
    public static SerialTaskExecutor create(Task firstTask) {
        return new SerialTaskExecutor(firstTask);
    }

    /**
     * コンストラクタ。最初のタスクを初期化し、シングルスレッドのExecutorServiceを作成する。
     *
     * @param firstTask 最初に実行するタスク
     * @throws IllegalArgumentException firstTaskがnullの場合
     */
    private SerialTaskExecutor(Task firstTask) {
        this(firstTask, Executors.newSingleThreadExecutor());
    }

    /**
     * カスタムExecutorServiceを使用するコンストラクタ。
     *
     * @param firstTask 最初に実行するタスク
     * @param executor  使用するExecutorService
     * @throws IllegalArgumentException firstTaskまたはexecutorがnullの場合
     */
    public SerialTaskExecutor(Task firstTask, ExecutorService executor) {
        if (firstTask == null || executor == null) {
            throw new IllegalArgumentException("firstTask and executor cannot be null");
        }
        this.firstTask = firstTask;
        this.executor = executor;
    }

    /**
     * 次のタスクを追加するメソッド。チェーンメソッドとして利用可能。
     *
     * @param addTask 追加するタスク
     * @return SerialTaskExecutorインスタンス
     */
    public SerialTaskExecutor addNextTask(Task addTask) {
        tasks.add(addTask);
        return this;
    }

    /**
     * タスクの実行を開始するメソッド。
     *
     * @param callback すべてのタスクが完了した後に呼び出されるコールバック
     */
    public void run(Consumer<JsonObject> callback) {

        // コンテキストとして利用するJsonObject
        JsonObject ctx = new JsonObject();

        // 最初のタスクを非同期に実行し、結果をハンドリング
        CompletableFuture<JsonObject> future = CompletableFuture.supplyAsync(firstTask, executor)
                .handle(handler(ctx, firstTask.taskName()));

        // 後続のタスクを順次実行するためのチェーンを作成
        for (Task task : this.tasks) {
            future = future.thenCompose(nextTask(ctx, task));
        }

        // 全てのタスクが完了した後の処理を定義
        future.thenAccept(finish(callback));
    }

    /**
     * タスクの結果をハンドリングするためのBiFunctionを返すメソッド。
     *
     * @param context  コンテキストオブジェクト
     * @param taskName タスクの名前
     * @return タスクの結果をハンドリングするBiFunction
     */
    private BiFunction<JsonObject, Throwable, JsonObject> handler(JsonObject context, String taskName) {
        return (responce, th) -> {
            if (th != null) {
                System.out.println("exception in " + taskName + ": " + th.getMessage());
                th.printStackTrace();
                return null;
            } else {
                context.add(taskName, responce);
                return context;
            }
        };
    }

    /**
     * 次のタスクを非同期に実行するためのFunctionを返すメソッド。
     *
     * @param context コンテキストオブジェクト
     * @param task    次に実行するタスク
     * @return 次のタスクを非同期に実行するFunction
     */
    private Function<JsonObject, CompletionStage<JsonObject>> nextTask(JsonObject context, Task task) {
        return responce -> {
            if (responce == null) {
                return CompletableFuture.completedFuture(null);
            } else {
                task.setContext(responce);
                return CompletableFuture.supplyAsync(task, executor)
                        .handle(handler(context, task.taskName()));
            }
        };
    }

    /**
     * 最終的な結果を処理するためのConsumerを返すメソッド。
     *
     * @param callback 結果を受け取るコールバック
     * @return 結果を処理するConsumer
     */
    private Consumer<JsonObject> finish(Consumer<JsonObject> callback) {
        return responce -> {
            callback.accept(responce);
            executor.shutdown();
        };
    }
}
