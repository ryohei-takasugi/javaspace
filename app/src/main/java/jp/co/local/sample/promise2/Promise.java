package jp.co.local.sample.promise2;

public class Promise<T> {

    private Future<T> future;

    public static <T> Promise<T> promise() {
        return new Promise<>();
    }

    public synchronized void complete(T result) {
        if (future == null) {
            this.future = new Future<>(result, null);
        } else {
            throw new IllegalStateException("Future has already been set.");
        }
    }

    public synchronized void fail(Throwable th) {
        if (future == null) {
            this.future = new Future<>(null, th);
        } else {
            throw new IllegalStateException("Future has already been set.");
        }
    }

    public Future<T> future() {
        if (future != null) {
            return this.future;
        } else {
            throw new IllegalStateException("Future has not been set.");
        }
    }
}
