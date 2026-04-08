package part7.part7_1;

import java.util.*;
import java.util.function.*;

/**
 * Задание 7.1 — Исходный код с анонимными классами
 *
 * НЕ ИЗМЕНЯЙТЕ этот файл. Он служит образцом для рефакторинга.
 *
 * Задача:
 *   1. Скопируйте логику в RefactorStep1.java и замените анонимные классы на лямбда-выражения.
 *   2. Скопируйте результат в RefactorStep2.java и замените лямбды на ссылки на методы (где возможно).
 *
 * Как запустить: нажмите ▶ рядом с main.
 */

public class RefactorOriginal {
    public static void main(String[] args) {
        List<String> cities = Arrays.asList("Москва", "Берлин", "Токио", "Нью-Йорк", "Париж");

        // 1. Сортировка по длине
        cities.sort(new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return Integer.compare(a.length(), b.length());
            }
        });

        // 2. Вывод каждого элемента
        cities.forEach(new Consumer<String>() {
            @Override
            public void accept(String city) {
                System.out.println(city);
            }
        });

        // 3. Преобразование в верхний регистр
        Function<String, String> toUpper = new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s.toUpperCase();
            }
        };

        // 4. Проверка длины > 5
        Predicate<String> isLong = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.length() > 5;
            }
        };

        // 5. Формирование строки с восклицательным знаком
        Function<String, String> exclaim = new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s + "!";
            }
        };

        // 6. Создание нового списка
        Supplier<List<String>> listFactory = new Supplier<List<String>>() {
            @Override
            public List<String> get() {
                return new ArrayList<>();
            }
        };

        // Использование
        List<String> result = listFactory.get();
        for (String city : cities) {
            if (isLong.test(city)) {
                result.add(toUpper.apply(city));
            }
        }
        System.out.println("Длинные города: " + result);
    }
}