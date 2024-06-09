package jp.co.local.sample.callable2;

public class MainLoop {

    public void run() {
        System.out.println("----------------------------");
        System.out.println("Thread Number: " + Thread.currentThread().getId());
        System.out.println("Thread Count1: " + Thread.activeCount());

        WatherApiTask task1 = new WatherApiTask(35.6895, 139.6917, "weather_code");
        WatherApiTask task2 = new WatherApiTask(34.6937, 135.5022, "weather_code");
        WatherApiTask task3 = new WatherApiTask(43.0667, 141.35, "weather_code");

        SerialTaskExecutor executor = SerialTaskExecutor.create(task1);
        executor.addNextTask(task2);
        executor.addNextTask(task3);
        executor.run(responce -> {
            System.out.println(responce);
        });
    }
}
