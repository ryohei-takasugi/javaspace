package jp.co.local.sample.function;

import com.google.gson.JsonObject;

public class MainLoop {
    public void run() {

        JsonObject json = new JsonObject();
        JsonObject sub1 = new JsonObject();
        sub1.addProperty("sub1", "sampleValue1");
        json.add("test", sub1);

        MyFunction<JsonObject, String> function1 = (JsonObject j) -> j.getAsJsonObject("test").get("sub1")
                .getAsString();

        // メソッドの呼び出し
        String sub1Value = function1.apply(json);
        System.out.println("sub1Value: " + sub1Value);
    }
}
