package jp.co.local.sample.promise;

import java.util.concurrent.Callable;

public class CallableTask implements Callable<String> {

    private String name;

    public CallableTask(String name) {
        this.name = name;
    }

    @Override
    public String call() throws Exception {
        System.out.println("Thread start");
        // ここに別スレッドで行いたい処理を書く
        // 今回はsleepで代用
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        System.out.println("Thread end");
        return "Hello " + name;
    }

}
