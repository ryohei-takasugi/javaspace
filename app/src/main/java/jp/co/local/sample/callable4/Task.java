package jp.co.local.sample.callable4;

import java.util.concurrent.CompletableFuture;

/**
 * タスクの実行を表すインターフェース。
 *
 * @param <T> タスクのコンテキストと結果の型
 */
public interface Task<T> {

    /**
     * タスクを非同期で実行します。
     *
     * @return CompletableFutureでラップされた非同期処理の結果
     */
    CompletableFuture<AsyncResult<T>> execute();

    /**
     * タスクの名前を返します。
     *
     * @return タスクの名前
     */
    String name();

    /**
     * タスクのコンテキストを設定します。
     *
     * @param context タスクのコンテキスト
     */
    void setContext(T context);
}
