package org.example;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class App {

    public static void main(String[] args) {
        List<Supplier<Boolean>> list  = Lists.newArrayList(
                () -> Boolean.FALSE,
                () -> Boolean.FALSE,
                () -> Boolean.FALSE
        );

        List<Supplier<Boolean>> list2  = Lists.newArrayList(
                () -> Boolean.FALSE,
                () -> Boolean.FALSE,
                () -> Boolean.FALSE
        );

        Optional<String> code = Optional.of(list.stream().map(Supplier::get)
                .reduce(Boolean.FALSE, (a, v) -> a || v))
                .filter(b -> b)
                .map(b -> "45");

        Optional<String> code2 = Optional.of(list2.stream().map(Supplier::get)
                .reduce(Boolean.FALSE, (a, v) -> a || v))
                .filter(b -> b)
                .map(b -> "54");

        System.out.println(
                code.orElse(
                        code2.orElse("EMP")
                )
        );
    }
}
