package jp.co.local.sample.callable2;

/**
 * AsyncResultクラスは、非同期処理の結果を保持するためのクラスです。
 * 処理が成功した場合の値、または失敗した場合の例外を保持します。
 *
 * @param <T> 結果の型
 */
public class AsyncResult<T> {

    private T value; // 非同期処理の結果の値
    private Throwable th; // 非同期処理の失敗原因となる例外

    public AsyncResult(T value, Throwable th) {
        this.value = value;
        this.th = th;
    }

    /**
     * 非同期処理の結果を取得します。
     * 
     * @return 非同期処理の結果
     */
    public T result() {
        return this.value;
    }

    /**
     * 非同期処理の失敗原因となる例外を取得します。
     * 
     * @return 非同期処理の失敗原因となる例外
     */
    public Throwable cause() {
        return th;
    }

    /**
     * 非同期処理が成功したかどうかを判定します。
     * 
     * @return 非同期処理が成功した場合はtrue、失敗した場合はfalse
     */
    public boolean succeeded() {
        return this.value != null;
    }

    /**
     * 非同期処理が失敗したかどうかを判定します。
     * 
     * @return 非同期処理が失敗した場合はtrue、成功した場合はfalse
     */
    public boolean failed() {
        return this.th != null;
    }
}
