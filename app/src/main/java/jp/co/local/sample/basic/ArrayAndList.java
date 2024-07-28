package jp.co.local.sample.basic;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class ArrayAndList {

    public void run() {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        System.out.print("forEach: ");
        list.forEach(number -> System.out.print(number + " "));
        System.out.println();

        List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        System.out.print("forEach: ");
        list2.forEach(number -> System.out.print(number + " "));
        System.out.println();

        boolean allMatch = list.stream().allMatch(num -> num > 3);
        System.out.println("allMatch: " + allMatch);

        boolean anyMathc = list.stream().anyMatch(num -> num > 3);
        System.out.println("anyMathc: " + anyMathc);

        Set<Integer> collect = list.stream().collect(Collectors.toSet());
        System.out.println("toSet: " + collect);

        long count = list.stream().count();
        System.out.println("count: " + count);

        List<Integer> map = list.stream().map(num -> num + 1).collect(Collectors.toList());
        System.out.println("map: " + map);

        List<Integer> limit = list.stream().limit(2).collect(Collectors.toList());
        System.out.println("limit: " + limit);

        List<Person> person = List.of(new Person("taro", 18), new Person("jiro", 17), new Person("hanako", 18));
        // Map<Integer, List<String>> personGroup =
        // person.stream().collect(Collectors.groupingBy(
        // Person::getAge,
        // Collectors.mapping(Person::getName, Collectors.toList())));
        Map<Integer, List<String>> personGroup = person.stream()
                .collect(
                        Collectors.groupingBy(p -> p.getAge(), // 年齢でグループ化
                                Collectors.mapping(
                                        p -> p.getName(), // 名前を取得
                                        Collectors.toList() // 名前のリストを収集
                                )));
        System.out.println("personGroup: " + personGroup);

        List<String> list3 = List.of("apple", "banana", "cherry");
        Optional<String> max = list3.stream().max((str1, str2) -> str1.compareTo(str2));
        System.out.println("max: " + max.get());
        Optional<String> min = list3.stream().min((str1, str2) -> str1.compareTo(str2));
        System.out.println("min: " + min.get());

        List<String> list4 = List.of("apple", "banana", "cherry", "banana");
        System.out.print("distinct: ");
        list4.forEach(str -> System.out.print(str + " "));
        System.out.println();
        List<String> distinct = list4.stream().distinct().collect(Collectors.toList());
        System.out.print("distinct: ");
        distinct.forEach(str -> System.out.print(str + " "));
        System.out.println();

        List<Integer> dropWhile = list.stream().dropWhile(number -> number < 5).collect(Collectors.toList());
        System.out.println("dropWhile: " + list);
        System.out.println("dropWhile: " + dropWhile);

        List<List<String>> list5 = List.of(list3, list4);
        List<String> flatMap = list5.stream().flatMap(l -> l.stream()).collect(Collectors.toList());
        System.out.println("flatMap: " + flatMap);

        List<String> list6 = Arrays.asList("1.1,2.2,3.3", "4.4,5.5,6.6");
        DoubleStream doubleStream = list6.stream()
                .flatMapToDouble(n -> Arrays.stream(n.split(",")).mapToDouble(s -> Double.parseDouble(s)));
        System.out.println("flatMapToDouble: " + doubleStream.sum());

        List<Integer> list7 = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        System.out.print("forEachOrdered: ");
        list7.parallelStream().forEachOrdered(number -> System.out.print(number + " "));
        System.out.println();
        System.out.print("forEach: ");
        list7.parallelStream().forEach(number -> System.out.print(number + " "));
        System.out.println();

        // List<Integer> numbers = IntStream.range(1,
        // 13).boxed().collect(Collectors.toList());
        // numbers.parallelStream()
        // .map(n -> {
        // System.out.println(
        // String.format("%10s : %2d - %s", "Parallel", n,
        // Thread.currentThread().getName()));
        // return n * 1;
        // })
        // .forEach(n -> {
        // try {
        // Thread.sleep(1000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // System.out.println(
        // String.format("%10s : %2d - %s", "Sequential", n,
        // Thread.currentThread().getName()));
        // });

        List<Integer> filter = list.stream()
                .filter(n -> n > 3)
                .collect(Collectors.toList());
        System.out.println("filter: " + filter);

        List<Integer> skipLimitSorted = list.stream()
                .skip(2).limit(10).sorted((n1, n2) -> n2 - n1)
                .collect(Collectors.toList());
        System.out.println("skipLimitSorted: " + skipLimitSorted);

        System.out.print("takeWhile: ");
        list.stream().takeWhile(n -> n < 5).forEach(n -> System.out.print(n + " "));
        System.out.println();

    }

    public class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}
