package jp.co.local.sample.loop;

import java.net.URI;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Config {

    private final URI url1;
    private final URI url2;
    private final URI url3;
    private final String test;
    private final String test1;

    public static Config create(Properties properties, JsonElement config) {
        return new Config(properties, config.getAsJsonObject());
    }

    private Config(Properties properties, JsonObject config) {
        this.url1 = URI.create(config.get("url1").getAsString());
        this.url2 = URI.create(config.get("url2").getAsString());
        this.url3 = URI.create(config.get("url3").getAsString());
        this.test = properties.getProperty("test");
        this.test1 = properties.getProperty("test1.aaa");
    }

    public URI getUrl1() {
        return this.url1;
    }

    public URI getUrl2() {
        return this.url2;
    }

    public URI getUrl3() {
        return this.url3;
    }

    public String test() {
        return this.test;
    }

    public String test1() {
        return this.test1;
    }

    public JsonObject toJSON() {
        JsonObject to = new JsonObject();
        to.addProperty("url1", this.url1.toString());
        to.addProperty("url2", this.url2.toString());
        to.addProperty("url3", this.url3.toString());
        to.addProperty("test", this.test);
        to.addProperty("test1", this.test1);
        return to;
    }

    public static CompletableFuture<Config> load(Path configFilePath, ExecutorService singleThread) {
        CompletableFuture<Config> resultFut = new CompletableFuture<Config>();

        PropertiesReader propertiesReader = new PropertiesReader();
        Properties properties = propertiesReader.read();

        AsyncFileReader fileReader = new AsyncFileReader(configFilePath);
        CompletableFuture<JsonElement> fileFut = fileReader.read();

        fileFut.thenAcceptAsync(json -> {
            Config config = Config.create(properties, json);
            resultFut.complete(config);
        }, singleThread).exceptionally(th -> {
            resultFut.completeExceptionally(th);
            return null;
        });

        // 非同期操作が完了するのを待つ
        fileFut.join();

        return resultFut;
    }
}