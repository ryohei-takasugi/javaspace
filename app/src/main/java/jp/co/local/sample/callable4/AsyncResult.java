package jp.co.local.sample.callable4;

/**
 * 非同期処理の結果を保持するクラス
 *
 * @param <T> 成功時の結果の型
 */
public class AsyncResult<T> {
    private final T value;
    private final Throwable error;

    public AsyncResult(T value, Throwable error) {
        this.value = value;
        this.error = error;
    }

    public T result() {
        return value;
    }

    public Throwable faild() {
        return error;
    }

    public boolean isSuccess() {
        return error == null;
    }

    public boolean isFailure() {
        return error != null;
    }

    public static <T> AsyncResult<T> success(T value) {
        return new AsyncResult<>(value, null);
    }

    public static <T> AsyncResult<T> failure(Throwable error) {
        return new AsyncResult<>(null, error);
    }

    public static <T> AsyncResult<T> failure(Class<T> type, Throwable error) {
        return new AsyncResult<>(null, error);
    }
}
