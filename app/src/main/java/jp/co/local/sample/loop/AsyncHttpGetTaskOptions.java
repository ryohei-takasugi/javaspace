package jp.co.local.sample.loop;

import java.net.URI;
import java.net.http.HttpClient;

import com.google.gson.JsonObject;

/**
 * 非同期HTTP GETリクエストのオプションを設定するクラス。
 */
public class AsyncHttpGetTaskOptions {
    private final String taskName;
    private final URI url;
    private final HttpClient client;
    private final JsonObject context;

    /**
     * AsyncHttpGetTaskOptionsのコンストラクタ。
     *
     * @param builder ビルダーインスタンス
     */
    private AsyncHttpGetTaskOptions(Builder builder) {
        this.taskName = builder.taskName;
        this.url = builder.url;
        this.client = builder.client;
        this.context = builder.context;
    }

    /**
     * タスク名を返します。
     *
     * @return タスク名
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * リクエストURLを返します。
     *
     * @return リクエストURL
     */
    public URI getUrl() {
        return url;
    }

    /**
     * HttpClientインスタンスを返します。
     *
     * @return HttpClientインスタンス
     */
    public HttpClient getClient() {
        return client;
    }

    /**
     * コンテキストを返します。
     *
     * @return コンテキスト
     */
    public JsonObject getContext() {
        return context;
    }

    /**
     * AsyncHttpGetTaskOptionsのビルダークラス。
     */
    public static class Builder {
        private String taskName;
        private URI url;
        private HttpClient client;
        private JsonObject context;

        /**
         * タスク名を設定します。
         *
         * @param taskName タスク名
         * @return このビルダーインスタンス
         */
        public Builder setTaskName(String taskName) {
            this.taskName = taskName;
            return this;
        }

        /**
         * リクエストURLを設定します。
         *
         * @param url リクエストURL
         * @return このビルダーインスタンス
         */
        public Builder setUrl(URI url) {
            this.url = url;
            return this;
        }

        /**
         * HttpClientインスタンスを設定します。
         *
         * @param client HttpClientインスタンス
         * @return このビルダーインスタンス
         */
        public Builder setClient(HttpClient client) {
            this.client = client;
            return this;
        }

        /**
         * コンテキストを設定します。
         *
         * @param context コンテキスト
         * @return このビルダーインスタンス
         */
        public Builder setContext(JsonObject context) {
            this.context = context;
            return this;
        }

        /**
         * AsyncHttpGetTaskOptionsインスタンスを構築します。
         *
         * @return AsyncHttpGetTaskOptionsインスタンス
         */
        public AsyncHttpGetTaskOptions build() {
            validate();
            return new AsyncHttpGetTaskOptions(this);
        }

        /**
         * ビルダーの設定を検証します。
         *
         * @throws IllegalArgumentException 設定が無効な場合
         */
        private void validate() {
            if (taskName == null || taskName.isEmpty()) {
                throw new IllegalArgumentException("Task name must not be null or empty");
            }
            if (url == null) {
                throw new IllegalArgumentException("URL must not be null or empty");
            }
            // try {
            // new URL(url); // URL形式のチェック
            // } catch (MalformedURLException e) {
            // throw new IllegalArgumentException("Invalid URL format", e);
            // }
            if (client == null) {
                throw new IllegalArgumentException("HttpClient must not be null");
            }
        }
    }
}
