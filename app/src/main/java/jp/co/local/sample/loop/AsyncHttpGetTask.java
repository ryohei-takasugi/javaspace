package jp.co.local.sample.loop;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * AsyncHttpGetTaskクラスは、非同期にHTTP GETリクエストを送信するタスクを定義します。
 */
public class AsyncHttpGetTask implements Task<Object> {

    private final String taskName;
    private final HttpRequest request;
    private final HttpClient client;
    private JsonObject context;
    private static final Gson gson = new Gson();

    /**
     * AsyncHttpGetTaskのコンストラクタ。
     *
     * @param options タスクオプションを含むAsyncHttpGetTaskOptionsインスタンス
     */
    public static AsyncHttpGetTask create(AsyncHttpGetTaskOptions options) {
        return new AsyncHttpGetTask(options);
    }

    /**
     * AsyncHttpGetTaskのコンストラクタ。
     *
     * @param options タスクオプションを含むAsyncHttpGetTaskOptionsインスタンス
     */
    public AsyncHttpGetTask(AsyncHttpGetTaskOptions options) {
        this.taskName = options.getTaskName();
        this.request = HttpRequest.newBuilder()
                .uri(options.getUrl())
                .GET()
                .build();
        this.client = options.getClient();
        this.context = options.getContext();
    }

    @Override
    public String name() {
        return this.taskName;
    }

    /**
     * コンテキストを設定します。
     *
     * @param context コンテキストデータ
     */
    @Override
    public void setContext(Object context) {
        this.context = (JsonObject) context;
    }

    /**
     * 非同期HTTP GETリクエストを送信し、結果を返します。
     *
     * @return CompletableFutureでラップされたAsyncResultオブジェクト
     */
    @Override
    public CompletableFuture<Object> execute() {
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        System.out.println(this.taskName + " Response received: " + response.statusCode());
                        if (response.statusCode() == 200) {
                            JsonObject jsonResponse = this.context != null ? this.context : new JsonObject();
                            JsonObject body = gson.fromJson(response.body(), JsonObject.class);
                            jsonResponse.add(this.taskName, body);
                            return jsonResponse;
                        } else {
                            return new IllegalAccessError(this.taskName + " HTTP error: " + response.statusCode());
                        }
                    } catch (Throwable th) {
                        th.addSuppressed(new IllegalAccessError(this.taskName));
                        return th;
                    }
                }).exceptionally(th -> {
                    th.addSuppressed(new IllegalAccessError(this.taskName));
                    return th;
                });
    }
}
