package jp.co.local.sample.promise2;

import com.google.gson.JsonObject;

public class MainLoop {

    public void run() {
        JsonObject ctx = new JsonObject();
        Future<JsonObject> sample = sample();
        sample.compose(responce -> {
            ctx.add("test", responce);
            System.out.println("Compose 1: " + responce);
            return sample2();
        }).compose(responce -> {
            ctx.add("test2", responce);
            System.out.println("Compose 2: " + responce);
            return sample3();
        }).onSuccess(responce -> {
            ctx.add("test3", responce);
            System.out.println("Compose 3: " + responce);
            System.out.println("Success: " + ctx);
        }).onFailure(th -> {
            System.out.println("Failure: " + th);
        });
    }

    private Future<JsonObject> sample() {
        Promise<JsonObject> promise = Promise.promise();
        JsonObject sample = new JsonObject();
        sample.addProperty("name", "test");
        promise.complete(sample);
        return promise.future();
    }

    private Future<JsonObject> sample2() {
        Promise<JsonObject> promise = Promise.promise();
        JsonObject sample = new JsonObject();
        sample.addProperty("name", "test2");
        promise.complete(sample);
        return promise.future();
    }

    private Future<JsonObject> sample3() {
        Promise<JsonObject> promise = Promise.promise();
        JsonObject sample = new JsonObject();
        sample.addProperty("name", "test3");
        promise.complete(sample);
        return promise.future();
    }
}
