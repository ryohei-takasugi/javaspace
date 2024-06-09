package jp.co.local.sample.promise2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Future<T> {

    private Consumer<T> successHandler;
    private Consumer<Throwable> failureHandler;
    private List<Function<T, Future<T>>> composeHandlers = new ArrayList<>();
    private final T result;
    private final Throwable th;

    public Future(T result, Throwable th) {
        this.result = result;
        this.th = th;
    }

    public void start() {
        if (th != null) {
            handleFailure(th);
        } else if (!composeHandlers.isEmpty()) {
            handleCompose();
        } else {
            handleSuccess(result);
        }
    }

    private void handleSuccess(T result) {
        if (successHandler != null) {
            successHandler.accept(result);
        } else {
            System.out.println("Success handler is not set.");
        }
    }

    private void handleFailure(Throwable th) {
        if (failureHandler != null) {
            failureHandler.accept(th);
        } else {
            System.out.println("Failure handler is not set.");
        }
    }

    private void handleCompose() {
        Future<T> current = this;
        for (Function<T, Future<T>> handler : composeHandlers) {
            try {
                current = handler.apply(current.result);
                if (current.th != null) {
                    handleFailure(current.th);
                    return;
                }
            } catch (Throwable th) {
                handleFailure(th);
                return;
            }
        }
        handleSuccess(current.result);
    }

    public Future<T> onSuccess(Consumer<T> handler) {
        this.successHandler = handler;
        return this;
    }

    public Future<T> onFailure(Consumer<Throwable> handler) {
        this.failureHandler = handler;
        return this;
    }

    public Future<T> compose(Function<T, Future<T>> handler) {
        this.composeHandlers.add(handler);
        return this;
    }
}
