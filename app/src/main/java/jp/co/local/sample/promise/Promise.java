package jp.co.local.sample.promise;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Promise {

    public void run() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            CallableTask task = new CallableTask("yucatio");

            //
            Future<String> future = executor.submit(task);

            // ここにメインスレッドで行う処理を書く

            try {
                // 結果の受け取り(処理が終わっていなければ待たされる)
                String result = future.get();

                System.out.println(result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } finally {
            executor.shutdown();
        }
    }

}
