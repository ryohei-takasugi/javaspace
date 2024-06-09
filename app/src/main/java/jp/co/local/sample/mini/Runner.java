package jp.co.local.sample.mini;

public class Runner implements Runnable {

    private final Handler<String> handler;
    private final String message;

    public Runner(Handler<String> handler, String message) {
        this.handler = handler;
        this.message = message;
    }

    @Override
    public void run() {
        this.handler.handle(message);
    }
}
