package jp.co.local.sample.callable3;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import com.google.gson.JsonObject;

/**
 * AsyncHttpGetTaskクラスは、非同期にHTTP GETリクエストを送信するタスクを定義します。
 */
public class AsyncHttpGetTask implements Supplier<CompletableFuture<AsyncResult<JsonObject>>> {
    private final String url;
    private final HttpClient client;
    private JsonObject context;

    /**
     * AsyncHttpGetTaskのコンストラクタ。
     *
     * @param url    リクエストを送信するURL
     * @param client 共有するHttpClientインスタンス
     */
    public AsyncHttpGetTask(String url, HttpClient client) {
        this.url = url;
        this.client = client;
    }

    /**
     * コンテキストを設定します。
     *
     * @param context コンテキストデータ
     */
    public void setContext(JsonObject context) {
        this.context = context;
    }

    /**
     * 非同期HTTP GETリクエストを送信し、結果を返します。
     *
     * @return CompletableFutureでラップされたAsyncResultオブジェクト
     */
    @Override
    public CompletableFuture<AsyncResult<JsonObject>> get() {
        AsyncResult<JsonObject> result = new AsyncResult<>();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        System.out.println("Response received: " + response.statusCode()); // コンソールに文字列を表示
                        JsonObject jsonResponse = new JsonObject();
                        jsonResponse.addProperty("statusCode", response.statusCode());
                        jsonResponse.addProperty("body", response.body());
                        if (context != null) {
                            jsonResponse.add("context", context);
                        }
                        result.setResult(jsonResponse);
                        return result;
                    })
                    .exceptionally(e -> {
                        System.out.println("Request failed: " + e.getMessage()); // エラーメッセージをコンソールに表示
                        result.setFailed(e);
                        return result;
                    });
        } catch (Throwable th) {
            return CompletableFuture.failedFuture(th);
        }

    }
}
