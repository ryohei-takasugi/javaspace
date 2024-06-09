package jp.co.local.sample.mini;

import java.util.LinkedList;
import java.util.Queue;

public class EventLoop {
    private final Queue<Runnable> eventQueue = new LinkedList<>();
    private boolean running = true;

    public void run() {
        while (running) {
            Runnable task;
            synchronized (eventQueue) {
                task = eventQueue.poll();
            }
            if (task != null) {
                task.run();
            }
        }
    }

    public void stop() {
        running = false;
    }

    public void addTask(Runnable task) {
        synchronized (eventQueue) {
            eventQueue.add(task);
        }
    }
}
