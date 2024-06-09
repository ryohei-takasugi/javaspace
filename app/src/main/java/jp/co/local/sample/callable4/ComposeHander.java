package jp.co.local.sample.callable4;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.google.gson.JsonObject;

public class ComposeHander implements Function<AsyncResult<JsonObject>, CompletableFuture<AsyncResult<JsonObject>>> {

    private final Task<JsonObject> task;

    public ComposeHander(Task<JsonObject> task) {
        this.task = task;
    }

    @Override
    public CompletableFuture<AsyncResult<JsonObject>> apply(AsyncResult<JsonObject> responce) {
        if (responce.isFailure()) {
            return CompletableFuture.completedFuture(responce);
        }
        task.setContext(responce.result());
        return task.execute();
    }

}
