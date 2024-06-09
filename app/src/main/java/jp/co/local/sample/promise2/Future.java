package jp.co.local.sample.promise2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 非同期処理の結果を管理するためのクラス。
 *
 * @param <T> 非同期処理の成功結果の型
 */
public class Future<T> {

    private Consumer<T> successHandler;
    private Consumer<Throwable> failureHandler;
    private final List<Function<T, Future<T>>> composeHandlers = new ArrayList<>();
    private final T result;
    private final Throwable th;

    /**
     * Futureクラスのコンストラクタ。
     *
     * @param result 非同期処理の成功結果
     * @param th     非同期処理の失敗原因となる例外
     */
    public Future(T result, Throwable th) {
        this.result = result;
        this.th = th;
    }

    /**
     * 非同期処理を開始します。成功または失敗のハンドラを呼び出します。
     *
     * @throws IllegalStateException successHandlerまたはfailureHandlerが設定されていない場合
     */
    private void start() {
        if (successHandler == null) {
            throw new IllegalStateException("Success handler is not set.");
        }
        if (failureHandler == null) {
            throw new IllegalStateException("Failure handler is not set.");
        }

        if (th != null) {
            handleFailure(th);
        } else if (!composeHandlers.isEmpty()) {
            handleCompose();
        } else {
            handleSuccess(result);
        }
    }

    /**
     * 非同期処理が成功したときのハンドラを設定します。
     *
     * @param handler 成功時のハンドラ
     * @return このFutureインスタンス
     * @throws IllegalArgumentException handlerがnullの場合
     */
    public Future<T> onSuccess(Consumer<T> handler) {
        if (handler == null) {
            throw new IllegalArgumentException("Success handler must not be null.");
        }
        this.successHandler = handler;
        if (checkStartEnable())
            start();
        return this;
    }

    /**
     * 非同期処理が失敗したときのハンドラを設定します。
     *
     * @param handler 失敗時のハンドラ
     * @return このFutureインスタンス
     * @throws IllegalArgumentException handlerがnullの場合
     */
    public Future<T> onFailure(Consumer<Throwable> handler) {
        if (handler == null) {
            throw new IllegalArgumentException("Failure handler must not be null.");
        }
        this.failureHandler = handler;
        if (checkStartEnable())
            start();
        return this;
    }

    /**
     * 非同期処理の結果に基づいて新しいFutureを生成するハンドラを追加します。
     *
     * @param handler 新しいFutureを生成するハンドラ
     * @return このFutureインスタンス
     */
    public Future<T> compose(Function<T, Future<T>> handler) {
        this.composeHandlers.add(handler);
        return this;
    }

    /**
     * 非同期処理を開始できるかどうかを確認します。
     *
     * @return 開始可能な場合はtrue、それ以外の場合はfalse
     */
    private boolean checkStartEnable() {
        if (this.successHandler != null && this.failureHandler != null
                && (this.result != null || this.th != null))
            return true;
        return false;
    }

    /**
     * 成功ハンドラを呼び出します。
     *
     * @param result 非同期処理の成功結果
     */
    private void handleSuccess(T result) {
        try {
            successHandler.accept(result);
        } catch (Throwable th) {
            System.err.println("Error in success handler: " + th.getMessage());
            th.printStackTrace();
        }
    }

    /**
     * 失敗ハンドラを呼び出します。
     *
     * @param th 非同期処理の失敗原因となる例外
     */
    private void handleFailure(Throwable th) {
        try {
            failureHandler.accept(th);
        } catch (Throwable handlerException) {
            System.err.println("Error in failure handler: " + handlerException.getMessage());
            handlerException.printStackTrace();
        }
    }

    /**
     * composeハンドラを順次処理します。
     */
    private void handleCompose() {
        Future<T> current = this;
        for (Function<T, Future<T>> handler : composeHandlers) {
            try {
                current = handler.apply(current.result);
                if (current.th != null) {
                    handleFailure(current.th);
                    return;
                }
            } catch (Throwable th) {
                handleFailure(th);
                return;
            }
        }
        handleSuccess(current.result);
    }
}
