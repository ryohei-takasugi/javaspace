package jp.co.local.sample.callable4;

/**
 * 非同期処理の結果を保持するクラス。
 *
 * @param <T> 成功時の結果の型
 */
public class AsyncResult<T> {
    private final T value;
    private final Throwable error;

    /**
     * AsyncResultクラスのコンストラクタ。
     *
     * @param value 成功時の結果
     * @param error 失敗時の例外
     */
    public AsyncResult(T value, Throwable error) {
        this.value = value;
        this.error = error;
    }

    /**
     * 成功時の結果を返します。
     *
     * @return 成功時の結果
     */
    public T result() {
        return value;
    }

    /**
     * 失敗時の例外を返します。
     *
     * @return 失敗時の例外
     */
    public Throwable faild() {
        return error;
    }

    /**
     * 非同期処理が成功したかどうかを判定します。
     *
     * @return 成功した場合はtrue、失敗した場合はfalse
     */
    public boolean isSuccess() {
        return error == null;
    }

    /**
     * 非同期処理が失敗したかどうかを判定します。
     *
     * @return 失敗した場合はtrue、成功した場合はfalse
     */
    public boolean isFailure() {
        return error != null;
    }

    /**
     * 成功時のAsyncResultインスタンスを生成します。
     *
     * @param <T>   成功時の結果の型
     * @param value 成功時の結果
     * @return 成功時のAsyncResultインスタンス
     */
    public static <T> AsyncResult<T> success(T value) {
        return new AsyncResult<>(value, null);
    }

    /**
     * 失敗時のAsyncResultインスタンスを生成します。
     *
     * @param <T>   成功時の結果の型
     * @param error 失敗時の例外
     * @return 失敗時のAsyncResultインスタンス
     */
    public static <T> AsyncResult<T> failure(Throwable error) {
        return new AsyncResult<>(null, error);
    }

    /**
     * 失敗時のAsyncResultインスタンスを生成します。
     *
     * @param <T>   成功時の結果の型
     * @param type  成功時の結果の型のクラス
     * @param error 失敗時の例外
     * @return 失敗時のAsyncResultインスタンス
     */
    public static <T> AsyncResult<T> failure(Class<T> type, Throwable error) {
        return new AsyncResult<>(null, error);
    }
}
