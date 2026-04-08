# Часть 9 — Эксперименты в jshell

## Как запустить jshell

Откройте терминал IntelliJ (View → Tool Windows → Terminal) и введите:
```
jshell
```
Для выхода: `/exit`

---

## Задание 9.1: Sealed-классы

### Команды (скопируйте и вставьте в jshell)

```
sealed interface Shape permits Circle, Square {}
record Circle(double r) implements Shape {}
record Square(double side) implements Shape {}
Shape s = new Circle(5)
s instanceof Circle c ? "Круг r=" + c.r() : "Не круг"
```

### Фактический вывод: (впишите результат выполнения каждой команды)

```
PS D:\IntelliJ IDEA 2025.3.2\projects\practice2> jshell
|  Welcome to JShell -- Version 17.0.18
|  For an introduction type: /help intro

jshell> sealed interface Shape permits Circle, Square {}
|  created interface Shape, however, it cannot be referenced until class Circle, and class Square are declared

jshell> record Circle(double r) implements Shape {}
|  created record Circle, however, it cannot be referenced until class Shape is declared

jshell> record Square(double side) implements Shape {}
|  created record Square

jshell> Shape s = new Circle(5)
s ==> Circle[r=5.0]

jshell> s instanceof Circle c ? "Круг r=" + c.r() : "Не круг"
$5 ==> "Круг r=5.0"
```

### Вопрос: Что произойдёт при попытке создать `record Triangle(double a) implements Shape {}`?

**Ваш ответ:**
```
jshell> record Triangle(double a) implements Shape {}
|  Error:
|  class is not allowed to extend sealed class: Shape (as it is not listed in its permits clause)
|  record Triangle(double a) implements Shape {}
|  ^-------------------------------------------^
```



---

## Задание 9.2: Цепочка лямбд

### Команды

```
import java.util.function.*
Function<String, String> trim = String::trim
Function<String, String> upper = String::toUpperCase
Function<String, String> exclaim = s -> s + "!"
var pipeline1 = trim.andThen(upper).andThen(exclaim)
var pipeline2 = exclaim.compose(upper).compose(trim)
pipeline1.apply("  hello world  ")
pipeline2.apply("  hello world  ")
```

### Фактический вывод:

```
jshell> import java.util.function.*

jshell> Function<String, String> trim = String::trim
trim ==> $Lambda$26/0x0000020c08013a00@5eb5c224

jshell> Function<String, String> upper = String::toUpperCase
upper ==> $Lambda$27/0x0000020c08013400@ea30797

jshell> Function<String, String> exclaim = s -> s + "!"
exclaim ==> $Lambda$28/0x0000020c08014200@aec6354

jshell> var pipeline1 = trim.andThen(upper).andThen(exclaim)
pipeline1 ==> java.util.function.Function$$Lambda$29/0x0000020c0805e5d8@726f3b58

jshell> var pipeline2 = exclaim.compose(upper).compose(trim)
pipeline2 ==> java.util.function.Function$$Lambda$30/0x0000020c0805e818@15615099

jshell> pipeline1.apply("  hello world  ")
$12 ==> "HELLO WORLD!"

jshell> pipeline2.apply("  hello world  ")
$13 ==> "HELLO WORLD!"
```

### Вопрос: Дают ли `andThen()` и `compose()` одинаковый результат? В каком случае результаты будут различаться?

**Ответ: `andThen()` и `compose()` дают одинаковый результат только когда функции коммутируют (порядок выполнения не влияет на результат). В общем случае порядок важен, и результаты будут разными.**

---

## Задание 9.3: Сравнение EnumSet и HashSet

### Команды

```
enum Color { RED, GREEN, BLUE, YELLOW, CYAN, MAGENTA, WHITE, BLACK }
var enumSet = java.util.EnumSet.of(Color.RED, Color.GREEN, Color.BLUE)
var hashSet = new java.util.HashSet<>(java.util.Set.of(Color.RED, Color.GREEN, Color.BLUE))
enumSet.contains(Color.RED)
hashSet.contains(Color.RED)
enumSet.getClass().getSimpleName()
hashSet.getClass().getSimpleName()
```

### Фактический вывод:

```
jshell> enum Color { RED, GREEN, BLUE, YELLOW, CYAN, MAGENTA, WHITE, BLACK}
|  created enum Color

jshell> var enumSet = java.util.EnumSet.of(Color.RED, Color.GREEN, Color.BLUE)
enumSet ==> [RED, GREEN, BLUE]

jshell> var hashSet = new java.util.HashSet<>(java.util.Set.of(Color.RED, Color.GREEN, Color.BLUE))
hashSet ==> [RED, BLUE, GREEN]

jshell> enumSet.contains(Color.RED)
$4 ==> true

jshell> hashSet.contains(Color.RED)
$5 ==> true

jshell> enumSet.getClass().getSimpleName()
$6 ==> "RegularEnumSet"

jshell> hashSet.getClass().getSimpleName()
$7 ==> "HashSet"
```

### Вопрос: Почему внутренний класс EnumSet называется `RegularEnumSet`? Что произойдёт, если enum будет иметь больше 64 констант?

**Ответ:**

**Внутренний класс `EnumSet` называется `RegularEnumSet` потому что он используется для обычных enum, в которых не больше 64 констант. Он хранит элементы в одном числе типа long (64 бита), где каждый бит отвечает за одну константу enum.**

**Если enum будет иметь больше 64 констант Java автоматически переключится на другую реализацию — JumboEnumSet. Она использует массив long[] (массив чисел), чтобы хранить больше 64 бит. Такой enum называется "гигантским" (jumbo).**