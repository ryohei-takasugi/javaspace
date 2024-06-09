package jp.co.local.sample.each;

import java.util.List;
import java.util.function.Consumer;

public class ListForEach {

    public void run() {
        List<String> list = List.of("a", "b", "c");

        // (1)
        list.forEach(new Consumer<String>() {
            @Override
            public void accept(String t) {
                System.out.println("ListForEach:" + "(1)" + t);
            }
        });

        // (2)
        list.forEach(s -> {
            System.out.println("ListForEach:" + "(2)" + s);
        });

        // (3)
        list.forEach(s -> System.out.println("ListForEach:" + "(3)" + s));

        // (4) メソッド参照
        list.forEach(System.out::println);
    }
}
