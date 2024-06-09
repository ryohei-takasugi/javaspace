package jp.co.local.sample.callable4;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;

import com.google.gson.JsonObject;

public class AsyncHttpGetTaskOptions {
    private final String taskName;
    private final String url;
    private final HttpClient client;
    private final JsonObject context;

    private AsyncHttpGetTaskOptions(Builder builder) {
        this.taskName = builder.taskName;
        this.url = builder.url;
        this.client = builder.client;
        this.context = builder.context;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getUrl() {
        return url;
    }

    public HttpClient getClient() {
        return client;
    }

    public JsonObject getContext() {
        return context;
    }

    public static class Builder {
        private String taskName;
        private String url;
        private HttpClient client;
        private JsonObject context;

        public Builder setTaskName(String taskName) {
            this.taskName = taskName;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setClient(HttpClient client) {
            this.client = client;
            return this;
        }

        public Builder setContext(JsonObject context) {
            this.context = context;
            return this;
        }

        public AsyncHttpGetTaskOptions build() {
            validate();
            return new AsyncHttpGetTaskOptions(this);
        }

        private void validate() {
            if (taskName == null || taskName.isEmpty()) {
                throw new IllegalArgumentException("Task name must not be null or empty");
            }
            if (url == null || url.isEmpty()) {
                throw new IllegalArgumentException("URL must not be null or empty");
            }
            try {
                new URL(url); // URL形式のチェック
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Invalid URL format", e);
            }
            if (client == null) {
                throw new IllegalArgumentException("HttpClient must not be null");
            }
        }
    }
}
