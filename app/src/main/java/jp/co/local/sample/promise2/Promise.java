package jp.co.local.sample.promise2;

/**
 * 非同期処理の結果を保持するためのクラス。
 *
 * @param <T> 非同期処理の結果の型
 */
public class Promise<T> {

    private Future<T> future;

    /**
     * 新しいPromiseインスタンスを生成します。
     *
     * @param <T> 非同期処理の結果の型
     * @return 新しいPromiseインスタンス
     */
    public static <T> Promise<T> promise() {
        return new Promise<>();
    }

    /**
     * 非同期処理が正常に完了したことを設定します。
     *
     * @param result 非同期処理の結果
     * @throws IllegalStateException Futureが既に設定されている場合
     */
    public synchronized void complete(T result) {
        if (future == null) {
            this.future = new Future<>(result, null);
        } else {
            throw new IllegalStateException("Future has already been set.");
        }
    }

    /**
     * 非同期処理が失敗したことを設定します。
     *
     * @param th 非同期処理の失敗原因となる例外
     * @throws IllegalStateException Futureが既に設定されている場合
     */
    public synchronized void fail(Throwable th) {
        if (future == null) {
            this.future = new Future<>(null, th);
        } else {
            throw new IllegalStateException("Future has already been set.");
        }
    }

    /**
     * Futureインスタンスを返します。
     *
     * @return このPromiseに関連付けられたFutureインスタンス
     * @throws IllegalStateException Futureがまだ設定されていない場合
     */
    public Future<T> future() {
        if (future != null) {
            return this.future;
        } else {
            throw new IllegalStateException("Future has not been set.");
        }
    }
}
