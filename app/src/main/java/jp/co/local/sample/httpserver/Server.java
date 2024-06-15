package jp.co.local.sample.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

public class Server {

    public void run() throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(18080), 10);

        server.createContext("/", new RootHandler());

        server.setExecutor(Executors.newCachedThreadPool()); // デフォルトのエグゼキュータを使用
        server.start();

        System.out.println(
                String.format("Server started on %s:%s ",
                        server.getAddress().getAddress(),
                        server.getAddress().getPort()));
    }
}
