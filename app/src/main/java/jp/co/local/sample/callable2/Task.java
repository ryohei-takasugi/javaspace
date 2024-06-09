package jp.co.local.sample.callable2;

import java.util.function.Supplier;

import com.google.gson.JsonObject;

public abstract class Task implements Supplier<JsonObject> {

    public abstract String taskName();

    public abstract Supplier<JsonObject> setContext(JsonObject ctx);
}
