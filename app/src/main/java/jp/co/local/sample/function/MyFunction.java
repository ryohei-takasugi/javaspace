package jp.co.local.sample.function;

@FunctionalInterface
public interface MyFunction<T, R> {
    R apply(T t);
}