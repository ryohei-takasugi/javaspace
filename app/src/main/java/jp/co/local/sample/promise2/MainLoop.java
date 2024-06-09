package jp.co.local.sample.promise2;

import com.google.gson.JsonObject;

public class MainLoop {

    public void run() {
        Future<JsonObject> sample = sample();
        sample.compose(responce -> {
            System.out.println("Compose 1: " + responce);
            return sample2();
        }).compose(responce -> {
            System.out.println("Compose 2: " + responce);
            return sample3();
        }).onSuccess(responce -> {
            System.out.println("Success: " + responce);
        }).onFailure(th -> {
            System.out.println("Failure: " + th);
        }).start();
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
