package jp.co.local.sample.callable4;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.google.gson.JsonObject;

/**
 * 非同期処理の結果を次のタスクに渡すためのハンドラ。
 */
public class ComposeHander implements Function<AsyncResult<JsonObject>, CompletableFuture<AsyncResult<JsonObject>>> {

    private final Task<JsonObject> task;

    /**
     * ComposeHanderのコンストラクタ。
     *
     * @param task 次に実行するタスク
     */
    public ComposeHander(Task<JsonObject> task) {
        this.task = task;
    }

    /**
     * 非同期処理の結果を次のタスクに渡し、次のタスクを実行します。
     *
     * @param responce 前のタスクの結果
     * @return 次のタスクの実行結果を含むCompletableFuture
     */
    @Override
    public CompletableFuture<AsyncResult<JsonObject>> apply(AsyncResult<JsonObject> responce) {
        if (responce.isFailure()) {
            return CompletableFuture.completedFuture(responce);
        }
        task.setContext(responce.result());
        return task.execute();
    }
}
