package jp.co.local.sample.callable4;

import java.util.concurrent.CompletableFuture;

public interface Task<T> {
    CompletableFuture<AsyncResult<T>> execute();

    String name();

    void setContext(T context);
}
