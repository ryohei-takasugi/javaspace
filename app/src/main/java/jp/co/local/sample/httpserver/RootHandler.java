package jp.co.local.sample.httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RootHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("-----------");

        JsonObject ctx = new JsonObject();
        ctx.addProperty("method", exchange.getRequestMethod());

        byte[] body = exchange.getRequestBody().readAllBytes();
        ctx.addProperty("body", new String(body));

        ctx.addProperty("client", exchange.getRemoteAddress().toString());
        ctx.addProperty("uri", exchange.getRequestURI().toString());

        exchange.getRequestHeaders().entrySet().stream()
                .forEach(header -> {
                    if (!Arrays.asList("Cookie", "Accept").contains(header.getKey())) {
                        ctx.addProperty(header.getKey(), header.getValue().get(0));
                    }
                });

        JsonArray cookie = new JsonArray();
        exchange.getRequestHeaders().getOrDefault("Cookie", new ArrayList<String>())
                .stream()
                .map(c -> c.split(";"))
                .forEach(aa -> {
                    for (String a : aa) {
                        JsonObject obj = new JsonObject();
                        String[] b = a.split("=");
                        obj.addProperty(b[0].strip(), b[1].strip());
                        cookie.add(obj);
                    }
                });
        ctx.add("Cookie", cookie);

        JsonArray accept = new JsonArray();
        exchange.getRequestHeaders().getOrDefault("Accept", new ArrayList<String>())
                .stream()
                .map(c -> c.split(","))
                .forEach(aa -> {
                    for (String a : aa) {
                        accept.add(a.strip());
                    }
                });
        ctx.add("Accept", accept);

        JsonArray lang = new JsonArray();
        exchange.getRequestHeaders().getOrDefault("Accept-language", new ArrayList<String>())
                .stream()
                .map(c -> c.split(","))
                .forEach(aa -> {
                    for (String a : aa) {
                        lang.add(a.strip());
                    }
                });
        ctx.add("Accept-language", lang);

        JsonArray enc = new JsonArray();
        exchange.getRequestHeaders().getOrDefault("Accept-encoding", new ArrayList<String>())
                .stream()
                .map(c -> c.split(","))
                .forEach(aa -> {
                    for (String a : aa) {
                        enc.add(a.strip());
                    }
                });
        ctx.add("Accept-encoding", enc);

        exchange.setAttribute("sample", "value");
        exchange.getHttpContext()
                .getAttributes()
                .forEach((key, val) -> ctx.addProperty(key, val.toString()));

        System.out.println(ctx.toString());

        exchange.sendResponseHeaders(200, ctx.toString().length());
        OutputStream os = exchange.getResponseBody();
        os.write(ctx.toString().getBytes());
        os.close();
    }

}
