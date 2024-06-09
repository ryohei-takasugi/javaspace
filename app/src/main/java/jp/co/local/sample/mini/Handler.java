package jp.co.local.sample.mini;

@FunctionalInterface
public interface Handler<E> {
    void handle(E context);
}
