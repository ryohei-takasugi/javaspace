package jp.co.local.sample.mini;

import java.util.LinkedHashMap;
import java.util.function.Consumer;

public class BlockingHandler {
    public void run() {
        EventLoop eventLoop = new EventLoop();

        // シンプルなハンドラの例
        Handler<String> printHandler = new PrintHandler();

        // イベントループにタスクを追加
        eventLoop.addTask(new Runner(printHandler, "Hello, World!"));
        eventLoop.addTask(new Runner(printHandler, "Another event!"));

        // 別スレッドでイベントループを実行
        new Thread(eventLoop::run).start();

        // 2秒後にイベントループを停止
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        eventLoop.stop();
    }

    public class PrintHandler implements Handler<String> {
        @Override
        public void handle(String message) {
            System.out.println("Event received: " + message);
        }
    }

    public class CallBackHandler implements Consumer<LinkedHashMap<String, Object>> {

        @Override
        public void accept(LinkedHashMap<String, Object> context) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'accept'");
        }

    }
}
