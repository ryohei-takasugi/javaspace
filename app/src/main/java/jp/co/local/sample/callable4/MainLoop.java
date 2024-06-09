package jp.co.local.sample.callable4;

import java.net.http.HttpClient;

import com.google.gson.JsonObject;

public class MainLoop {
    public void run() {

        HttpClient client = HttpClient.newHttpClient();

        try {
            AsyncHttpGetTaskOptions options1 = new AsyncHttpGetTaskOptions.Builder()
                    .setTaskName("task1")
                    .setUrl("https://jsonplaceholder.typicode.com/todos/1")
                    .setClient(client)
                    .setContext(new JsonObject())
                    .build();

            AsyncHttpGetTaskOptions options2 = new AsyncHttpGetTaskOptions.Builder()
                    .setTaskName("task2")
                    .setUrl("https://jsonplaceholder.typicode.com/todos/2")
                    .setClient(client)
                    .build();

            AsyncHttpGetTaskOptions options3 = new AsyncHttpGetTaskOptions.Builder()
                    .setTaskName("task3")
                    .setUrl("https://jsonplaceholder.typicode.com/todos/3")
                    .setClient(client)
                    .build();

            SerialTaskExecutor executor = SerialTaskExecutor.create(AsyncHttpGetTask.create(options1));
            executor.addTask(AsyncHttpGetTask.create(options2));
            executor.addTask(AsyncHttpGetTask.create(options3));

            executor.executeTasks(result -> {
                if (result.isFailure()) {
                    System.err.println("Final result contains an error:");
                    result.faild().printStackTrace();
                } else {
                    System.out.println("Final result: " + result.result());
                }
                System.out.println("All tasks completed.");
            });
        } catch (Throwable th) {
            System.err.println("Error creating task options: " + th.getMessage());
            th.printStackTrace();
        }

    }
}
