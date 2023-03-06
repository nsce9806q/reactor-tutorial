# Reactive Programming with Reactor 3
JVM에서 동작하는 non-blocking 어플리케이션을 위한 [Reactive Streams](https://a-day-of-mercury.tistory.com/entry/1-Reactive-Streams) 스펙의 Reactor 라이브러리를 학습하고 정리한 레포지토리입니다.

Reactor에서 제공하는 [Hands-on](https://tech.io/playgrounds/929/reactive-programming-with-reactor-3/Intro)을 기반으로 실습하였습니다.

## Get started
1. 환경
   - JDK 17
   - Build System: Gradle
2. build.gradle에 reactor-core 라이브러리 추가
```
dependencies {
    implementation 'io.projectreactor:reactor-core:3.5.3'
}
```
## Create Flux Instances
- `static <T> Flux<T> empty()`: 아무 데이터도 발행(emit)하지 않고 완료하는 Flux객체를 생성한다.
- `static <T> Flux<T> just(T... data)`: 1개 이상의 데이터를 발행하는 Flux객체를 생성하고 완료한다.
- `static <T> Flux<T> fromIterable(Iterable<? extends T> it)`: (List같은) Iterable 객체에 있는 데이터를 발행하는 Flux 객체를 생성한다.
- `static <T> Flux<T> error(Throwable error)`: 지정된 오류와 함께 완료하는 Flux 객체를 생성한다.
- `static Flux<Long> interval(Duration period)`: 매개변수로 받는 주기마다 데이터(Long타입) 0부터 증가시키면서 발행한다. `final Flux<T> take(Long n)`메소드로 데이터 개수를 지정하고 완료할 수 있다.

src/main/java/study/practice/Part01Flux.java
```java
public class Part01Flux {
  
  // TODO Return an empty Flux
  Flux<String> emptyFlux() {
    return Flux.empty();
  }
  
  // TODO Return a Flux that contains 2 values "foo" and "bar" without using an array or a collection
  Flux<String> fooBarFluxFromValues() {
    return Flux.just("foo", "bar");
  }
  
  // TODO Create a Flux from a List that contains 2 values "foo" and "bar"
  Flux<String> fooBarFluxFromList() {
    List<String> list = new ArrayList<>();
    list.add("foo");
    list.add("bar");
    return Flux.fromIterable(list);
  }
  
  // TODO Create a Flux that emits an IllegalStateException
  Flux<String> errorFlux() {
    return Flux.error(new IllegalStateException());
  }

  // TODO Create a Flux that emits increasing values from 0 to 9 each 100ms
  Flux<Long> counter() {
    return Flux.interval(Duration.ofMillis(100)).take(10);
  }
}
```