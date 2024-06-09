package jp.co.local.sample.callable3;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * AsyncResultクラスは、非同期処理の結果を保持するためのクラスです。
 * 処理が成功した場合の値、または失敗した場合の例外を保持します。
 *
 * @param <T> 結果の型
 */
public class AsyncResult<T> {

    private T value; // 非同期処理の結果の値
    private Throwable th; // 非同期処理の失敗原因となる例外

    /**
     * 非同期処理の結果を設定するコンストラクタ。
     *
     * @param value 非同期処理の結果
     * @param th    非同期処理の失敗原因となる例外
     */
    public AsyncResult(T value, Throwable th) {
        this.value = value;
        this.th = th;
    }

    // デフォルトコンストラクタ
    public AsyncResult() {
    }

    /**
     * 非同期処理の結果を設定します。
     *
     * @param value 非同期処理の結果
     */
    public void setResult(T value) {
        if (this.th == null) {
            this.value = value;
        }
    }

    /**
     * 非同期処理の失敗原因となる例外を設定します。
     *
     * @param th 非同期処理の失敗原因となる例外
     */
    public void setFailed(Throwable th) {
        if (this.value == null) {
            this.th = th;
        }
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

    /**
     * CreateResultクラスは、非同期処理の結果を生成するためのクラスです。
     * 非同期処理の結果と例外を入力として受け取り、AsyncResultオブジェクトを返します。
     *
     * @param <T> 結果の型
     */
    public static class CreateResult<T> implements BiFunction<T, Throwable, AsyncResult<T>> {

        /**
         * 非同期処理の結果と例外を入力として受け取り、AsyncResultオブジェクトを生成します。
         *
         * @param result 非同期処理の結果
         * @param th     非同期処理の失敗原因となる例外
         * @return AsyncResultオブジェクト
         */
        @Override
        public AsyncResult<T> apply(T result, Throwable th) {
            return new AsyncResult<>(result, th);
        }
    }

    /**
     * CreateResultのインスタンスを生成するスタティックメソッド。
     *
     * @param <T> 結果の型
     * @return CreateResultのインスタンス
     */
    public static <T> CreateResult<T> createResult() {
        return new CreateResult<>();
    }

    /**
     * Functionを生成するスタティックメソッド。
     *
     * @param <T> 結果の型
     * @return Functionのインスタンス
     */
    public static <T> Function<AsyncResult<T>, AsyncResult<T>> instance() {
        return ar -> ar;
    }
}
