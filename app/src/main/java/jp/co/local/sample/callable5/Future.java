package jp.co.local.sample.callable5;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Future<T> {

    private CompletableFuture<T> future;

    public Future(Supplier<T> supplier) {
        this.future = CompletableFuture.supplyAsync(supplier);
    }

    public Future<T> compose(Function<T, CompletableFuture<T>> function) {
        this.future = future.thenComposeAsync(function);
        return this;
    }

    public Future<T> onComplete(BiConsumer<T, Throwable> action) {
        this.future.whenCompleteAsync(action);
        return this;
    }

    public static <T> CompletableFuture<T> successFuture(T value) {
        return CompletableFuture.completedFuture(value);
    }

    public static <T> CompletableFuture<T> faildFuture(Throwable th) {
        return CompletableFuture.failedFuture(th);
    }
}
