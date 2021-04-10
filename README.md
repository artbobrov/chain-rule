# Chain Rule

## Парсинг

С помощью `antlr`, а трансформации его в свой intermediate representation(`CRIRElement` и его наследники(чуть-чуть похож на Psi)). whitespaces
инорируются и не сохраняются в ir.

Грамматика, конечно чуть-чуть поменялась, добавились унарные операции, внешние скобки не обязательны и, конечно, 
приоритет операций(взят из [C](https://en.cppreference.com/w/c/language/operator_precedence))

По грамматике из задания сгенерировал тесты с помощью [bnfgen](https://github.com/dmbaturin/bnfgen).

## Type Inference

Также реализован type inference с тестами.

Например:

```bash
λ ./gradlew run -q --args="--code 'map{element} %>% map{element+1}'"
IR text: map{element}%>%map{element+1}
map{element} %>% map{element+1} :: [Int] -> [Int]
```

В случае ошибки выкидывается исключение.

```bash
λ ./gradlew run -q --args="'map{element+1}%>%map{element & (1 > 0)}'"
IR text: map{element+1}%>%map{element&(1>0)}
Exception in thread "main" errors.TypeCheckError: TYPE ERROR. Types Int are not compatible with Boolean
        at typeInference.CRTypeContext.add(CRTypeContext.kt:70)
        at typeInference.CRTypeContext.plusAssign(CRTypeContext.kt:13)
        at typeInference.CRTypeInferenceVisitor.visitCallComposition(CRTypeInferenceVisitor.kt:105)
        at typeInference.CRTypeInferenceVisitor.visitCallComposition(CRTypeInferenceVisitor.kt:7)
        at ir.CRCallComposition.accept(CRCallComposition.kt:18)
        at MainKt.main(Main.kt:45)

FAILURE: Build failed with an exception.
```

```bash
λ ./gradlew run -q --args="-m 'map{element}%>%map{element=element}%>%map{element+1}'"
IR text: map{element}%>%map{element=element}%>%map{element+1}
Exception in thread "main" errors.TypeCheckError: TYPE ERROR. Types Boolean are not compatible with Int
        at typeInference.CRTypeContext.add(CRTypeContext.kt:70)
        at typeInference.CRTypeContext.plusAssign(CRTypeContext.kt:13)
        at typeInference.CRTypeInferenceVisitor.visitCallComposition(CRTypeInferenceVisitor.kt:105)
        at typeInference.CRTypeInferenceVisitor.visitCallComposition(CRTypeInferenceVisitor.kt:7)
        at ir.CRCallComposition.accept(CRCallComposition.kt:18)
        at MainKt.main(Main.kt:45)
```

## Трансформации

* Constant Folding

```bash
λ ./gradlew run -q --args="-c 'map{element+1+1+1+2*5}'"
IR text: map{element+1+1+1+2*5}
map{element+1+1+1+2*5} :: [Int] -> [Int]
Constant Folded: map{13+element}
```

```bash
λ ./gradlew run -q --args="-c 'map{element+1+1+1+2*5}%>%map{element>0|1<10}'"
IR text: map{element+1+1+1+2*5}%>%map{element>0|1<10}
map{element+1+1+1+2*5}%>%map{element>0|1<10} :: [Int] -> [Boolean]
Constant Folded: map{13+element}%>%map{0=0}
```

Boolean констаны я не вводил, поэтому использую `0=0` для `true` и `1=0` для `false`.

* Filter Folding

Сворачиваем все подряд идущие фильтры в конъюнкцию предикатов

```bash
λ ./gradlew run -q --args="-f 'filter{element+1>0}%>%filter{element=0}%>%map{element}'"
IR text: filter{element+1>0}%>%filter{element=0}%>%map{element}
filter{element+1>0}%>%filter{element=0}%>%map{element} :: [Int] -> [Int]
Filter Folded: filter{(element+1>0)&(element=0)}%>%map{element}
```

* Map Propagation

Убираем все map из цепочки, по порядку подставляем накопленное выражение в следующий элемент цепочки. В конце создаем
один большой map из накомплений.

```bash
λ ./gradlew run -q --args="-m 'map{element+1}%>%map{(element*2)=0}%>%filter{element|element}'"
IR text: map{element+1}%>%map{(element*2)=0}%>%filter{element|element}
map{element+1}%>%map{(element*2)=0}%>%filter{element|element} :: [Int] -> [Boolean]
Map Propagated: filter{(((element+1)*2)=0)|(((element+1)*2)=0)}%>%map{((element+1)*2)=0}
```