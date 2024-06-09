package jp.co.local.sample.callable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WatherApiCallable implements Callable<JsonObject> {

    private static final String BASE_URL = "https://api.open-meteo.com/v1/forecast";
    private final String latitude;
    private final String longitude;
    private final String current;
    private final JsonArray ctx = new JsonArray();

    public WatherApiCallable(Double latitude, Double longitude, String current) {
        if (latitude == null) {
            throw new IllegalArgumentException("latitude");
        }
        if (longitude == null) {
            throw new IllegalArgumentException("longitude");
        }
        if (current == null) {
            throw new IllegalArgumentException("current");
        }

        this.latitude = latitude.toString();
        this.longitude = longitude.toString();
        this.current = current;
    }

    public WatherApiCallable setContext(JsonArray ctx) {
        for (Iterator<JsonElement> itr = ctx.iterator(); itr.hasNext();) {
            this.ctx.add(itr.next());
        }
        System.out.println("setContext: " + this.ctx);
        return this;
    }

    @Override
    public JsonObject call() throws Exception {
        System.out.println("----------------------------");
        System.out.println("Thread Number: " + Thread.currentThread().getId());
        System.out.println("call context: " + this.ctx);
        JsonObject reply = new JsonObject();

        try {
            URI uri = URI
                    .create(BASE_URL + "?latitude=" + latitude + "&longitude=" + longitude + "&current=" + current);
            System.out.println(uri.toString());

            HttpClient client = HttpClient.newBuilder()
                    .version(Version.HTTP_1_1)
                    .followRedirects(Redirect.NORMAL)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .build();

            CompletableFuture<HttpResponse<String>> future = client.sendAsync(request, BodyHandlers.ofString());
            HttpResponse<String> responce = future.get(30000L, TimeUnit.MILLISECONDS);

            reply = JsonParser.parseString(responce.body()).getAsJsonObject();

        } catch (Throwable th) {
            th.printStackTrace();

        }

        return reply;
    }

}
