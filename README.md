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
## #1 Create Flux Instances
**Flux는 Reactive Streams의 Publisher를 구현하며 Flux Sequence를 생성, 변형, 조합하는 연산자들을 내장하는 클래스이다.**  
Flux는 **0-N개**의 데이터를 발행(emit)하고 (데이터를 발행할 때 마다 `onNext` 이벤트를 발생시킨다) 완료(`onComplete`) 또는 에러처리(`onError`)한다. (terminal event가 발생할 때까지 Flux는 무한하다.)

### 메소드 정리
- `static <T> Flux<T> empty()`: 아무 데이터도 발행하지 않고 완료하는 Flux 객체를 생성한다.
- `static <T> Flux<T> just(T... data)`: 1개 이상의 데이터를 발행하는 Flux 객체를 생성하고 완료한다.
- `static <T> Flux<T> fromIterable(Iterable<? extends T> it)`: (List같은) Iterable 객체에 있는 데이터를 발행하는 Flux 객체를 생성한다.
- `static <T> Flux<T> error(Throwable error)`: 지정된 오류와 함께 완료하는 Flux 객체를 생성한다.
- `static Flux<Long> interval(Duration period)`: 매개변수로 받는 주기마다 데이터(Long타입) 0부터 증가시키면서 발행한다. `final Flux<T> take(Long n)`메소드로 데이터 개수를 지정하고 완료할 수 있다.

### 예제: src/main/java/study/practice/**Part01Flux.java**
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

## #2 Create Mono Instances
Mono 또한 마찬가지로 Reactive Streams의 Publisher를 구현하고 여러 연산자들을 포함하지만, **Flux와 달리 0-1개만의 데이터를 발행한다.**  
그래서 Mono는 valued(1개의 데이터를 발행하고 완료), empty(데이터 발행없이 완료), error(에러 처리) 중에 하나다.

### 메소드 정리
- `static <T> Mono<T> empty()`: 아무 데이터도 발행하지 않고 완료하는 Mono 객체를 생성한다.
- `static <T> Mono<T> never()`: 아무 데이터도 발행하지 않고 완료 또는 에러를 포함한 아무 시그널도 보내지 않는 Mono 객체를 생성한다.
- `static <T> Mono<T> just(T data)`: 1개의 데이터를 발행하는 Mono 객체를 생성하고 완료한다.
- `static <T> Mono<T> error(Throwable error)`: 지정된 오류와 함께 완료하는 Mono 객체를 생성한다.

### 예제: src/main/java/study/practice/**Part02Mono.java**
```java
public class Part02Mono {
  
  // TODO Return an empty Mono
  Mono<String> emptyMono() {
    return Mono.empty();
  }
  
  // TODO Return a Mono that never emits any signal
  Mono<String> monoWithNoSignal() {
    return Mono.never();
  }

  // TODO Return a Mono that contains a "foo" value
  Mono<String> fooMono() {
    return Mono.just("foo");
  }
  
  // TODO Create a Mono that emits an IllegalStateException
  Mono<String> errorMono() {
    return Mono.error(new IllegalStateException());
  }

}
```
## #3 StepVerifier
`Mono`나 `Flux`와 같은 Publisher을 테스트 할 때에는 `reactor-test` 라이브러리의 `StepVerifier` 클래스를 사용할 수 있다.
테스트에서 발생된 이벤트가 기대(expectation)와 다를 때, `StepVerifier`은 `AssertionError`를 발생시킨다.

정적 팩토리 메서드 `create`로 `StepVerifier`의 인스턴스를 생성할 수 있다.  
그리고 마지막에는 반드시 `verify`(`verifyComplete()`, `verifyError()` 등) 메서드를 호출 하여야만 한다.

### 메소드 정리
- `Step<T> expectNext(T t)`: Publisher가 발행하는 데이터와 **기대값(t)을 검증**한다.
- `Step<T> assertNext(Consumer<? super T> assertionConsumer)`: AssertionJ나 Junit 같은 테스트 라이브러리를 사용하여 **Consumer 내부에서 검증**할 수 있다. Publisher가 발행하는 데이터의 속성(property)을 검증할 때 활용 할 수 있다.
- `Step<T> expectNextCount(long count)`: Publisher가 발행하는 **데이터의 수(count)를 검증**한다.
- `Step<T> expectSubscription()`: Publisher를 subscription 하는 이벤트가 있었는지 검증한다.
- `Step<T> thenAwait(Duration timeshift)`: 지정된 시간(timeshift)동안 **테스트와 Publisher thread를 일시 중지**한다.
- `Step<T> expectNoEvent(Duration duration)`: 지정된 시간(duration)동안 **아무 이벤트가 없음을 검증**한다.

아래의 마지막 예제(expect3600Elements)처럼 데이터를 모두 발행하는데 많은 시간이 필요한 테스트는 `withVirtualTime` 메서드를 활용할 수 있다.  
이 뒤에는 `thenAwait(Duration)` 또는 `expectNoEvent(Duration)` 메소드를 반드시 호출 해서 테스트 시간을 가속한다.  
(`expectNoEvent`의 경우 subscription 이벤트가 발생하므로 앞에 `expectSubscription`를 붙이면 된다.)

### 예제: src/main/java/study/practice/**Part03StepVerifier.java**
```java
public class Part03StepVerifier {
  
  // TODO Use StepVerifier to check that the flux parameter emits "foo" and "bar" elements then completes successfully.
  void expectFooBarComplete(Flux<String> flux) {
    StepVerifier.create(flux).expectNext("foo","bar").verifyComplete();
  }
  
  // TODO Use StepVerifier to check that the flux parameter emits "foo" and "bar" elements then a RuntimeException error.
  void expectFooBarError(Flux<String> flux) {
    StepVerifier.create(flux).expectNext("foo","bar").verifyError(RuntimeException.class);
  }
  
  // TODO Use StepVerifier to check that the flux parameter emits a User with "swhite"username
  // and another one with "jpinkman" then completes successfully.
  void expectSkylerJesseComplete(Flux<User> flux) {
    StepVerifier.create(flux).assertNext(user -> assertThat(user.getUsername()).isEqualTo("swhite"))
        .assertNext(user -> assertThat(user.getUsername()).isEqualTo("jpinkman")).verifyComplete();
  }
  
  // TODO Expect 10 elements then complete and notice how long the test takes.
  void expect10Elements(Flux<Long> flux) {
    StepVerifier.create(flux).expectNextCount(10).verifyComplete();
  }
  
  // TODO Expect 3600 elements at intervals of 1 second, and verify quicker than 3600s
  // by manipulating virtual time thanks to StepVerifier#withVirtualTime, notice how long the test takes
  void expect3600Elements(Supplier<Flux<Long>> supplier) {
    StepVerifier.withVirtualTime(supplier).thenAwait(Duration.ofHours(1)).expectNextCount(3600).verifyComplete();
  }

  private void fail() {
    throw new AssertionError("workshop not implemented");
  }

}
```
## #4 Transfrom
Reactor는 데이터를 변환(transform)할 때 몇가지의 연산자를 제공한다.

### 메소드 정리
- `<R> Flux<R> map(Function<? super T, ? extends R> mapper)`: flux에서 발행하는 데이터들을 **동기적으로 1대1 변환**한다.
- `<R> Flux<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> mapper)`: flux에서 발행하는 데이터들을 **비동기적으로 Publisher로 변환**하고, 합쳐서 **하나의 flux로 반환**한다.

`map`을 사용하면 람다 변환 함수를 통해 flux가 발행하는 데이터를 변환할 수 있다.  
만약에 외부 API를 사용하는 것처럼 변환 함수에 지연(latency)이 있다면 `flatmap`을 사용하면 된다.  
  
`map`을 사용하여 flux가 발행하는 데이터를 `Publisher`로 변환한다면, `Flux<Publisher<T>>`가 되지만, `flatmap`을 사용한다면
Publisher를 모아 하나의 `Flux<T>`로 합쳐준다.

### 예제: src/main/java/study/practice/**Part04Transform.java**
```java
public class Part04Transform {
  
  // TODO Capitalize the user username, firstname and lastname
  Mono<User> capitalizeOne(Mono<User> mono) {
    return mono.map(user -> new User(user.getUsername().toUpperCase(), user.getFirstname().toUpperCase(),
        user.getLastname().toUpperCase()));
  }

  // TODO Capitalize the users username, firstName and lastName
  Flux<User> capitalizeMany(Flux<User> flux) {
    return flux.map(user -> new User(user.getUsername().toUpperCase(), user.getFirstname().toUpperCase(),
        user.getLastname().toUpperCase()));
  }
  
  // TODO Capitalize the users username, firstName and lastName using #asyncCapitalizeUser
  Flux<User> asyncCapitalizeMany(Flux<User> flux) {
    return flux.flatMap(this::asyncCapitalizeUser);
  }

  Mono<User> asyncCapitalizeUser(User u) {
    return Mono.just(new User(u.getUsername().toUpperCase(), u.getFirstname().toUpperCase(), u.getLastname().toUpperCase()));
  }

}
```
## #5 Merge
아래 메소드를 사용해서 Publisher을 하나의 Flux로 합칠 수 있다.
### 메소드 정리
- `Flux<T> mergeWith(Publisher<? extends T> other)` : Publisher와 또 다른 Publisher(other)를 하나의 Flux로 합친다. **데이터의 순서가 보장되지 않는다.** (with interleave)
- `Flux<T> concatWith(Publisher<? extends T> other)`: Publisher와 또 다른 Publisher를(other) 하나의 Flux로 합친다. **데이터의 순서를 지킨다.** (no interleave)
### 예제: src/main/java/study/practice/Part05Merge.java
```java
public class Part05Merge {
  
   // TODO Merge flux1 and flux2 values with interleave
   Flux<User> mergeFluxWithInterleave(Flux<User> flux1, Flux<User> flux2) {
      return flux1.mergeWith(flux2);
   }
   
   // TODO Merge flux1 and flux2 values with no interleave (flux1 values and then flux2 values)
   Flux<User> mergeFluxWithNoInterleave(Flux<User> flux1, Flux<User> flux2) {
      return flux1.concatWith(flux2);
   }
   
   // TODO Create a Flux containing the value of mono1 then the value of mono2
   Flux<User> createFluxFromMultipleMono(Mono<User> mono1, Mono<User> mono2) {
      return mono1.concatWith(mono2);
   }

}
```
## #6 Request
Reactive Streams에는 **back pressure**라는 개념이 있는데, 이는 `Subscriber`가 받아서 처리할 데이터의 양을 `Publisher`에게 알려줌으로써 조절하는 반응 매커니즘이다.  
데이터 수요(demand)는 `Subscription` 단계에서 완료된다.  
`Subscription`은 `subscribe()` 호출에 의해 생성되며, `cancel()` 또는 `request()`에 의해 조작된다.

### 메소드 정리
- `static <T> FirstStep<T> create(Publisher<? extends T> publisher, long n)`: 받을 데이터의 수(n)를 설정하여 StepVerifier 인스턴스를 생성한다.
- `Flux<T> log()`: Flux를 로깅한다. 

### 예제: src/main/java/study/practice/Part06Request.java
```java
public class Part06Request {

   ReactiveRepository<User> repository = new ReactiveUserRepository();

   // TODO Create a StepVerifier that initially requests all values and expect 4 values to be received
   StepVerifier requestAllExpectFour(Flux<User> flux) {
      return StepVerifier.create(flux).expectNextCount(4).expectComplete();
   }

   // TODO Create a StepVerifier that initially requests 1 value and expects User.SKYLER then requests another value and expects User.JESSE then stops verifying by cancelling the source
   StepVerifier requestOneExpectSkylerThenRequestOneExpectJesse(Flux<User> flux) {
      return StepVerifier.create(flux)
              .thenRequest(1).expectNext(User.SKYLER)
              .thenRequest(1).expectNext(User.JESSE)
              .thenCancel();
   }

   // TODO Return a Flux with all users stored in the repository that prints automatically logs for all Reactive Streams signals
   Flux<User> fluxWithLog() {
      return repository.findAll().log();
   }

   // TODO Return a Flux with all users stored in the repository that prints "Starring:" at first, "firstname lastname" for all values and "The end!" on complete
   Flux<User> fluxWithDoOnPrintln() {
      return repository.findAll()
              .doFirst(() -> System.out.println("Starring:"))
              .doOnNext(user -> System.out.println(user.getFirstname() + " " + user.getLastname()))
              .doOnComplete(() -> System.out.println("The end!"));
   }
   
}
```
## #7 Error
### 메소드 정리
- `<E extends Throwable> Mono<T> onErrorReturn(Class<E> type, T fallbackValue)`: 에러(type)가 발생하면 다른 데이터(fallbackValue)를 전달한다.
- `Flux<T> onErrorResume(Function<? super Throwable, ? extends Publisher<? extends T>> fallback)`: 에러가 발생하면 함수를 사용하여 다른 데이터(Publisher 형태로)를 전달한다.

`Unchecked Exception`(Runtime Exception, **위 메소드는 Unchecked Exception을 처리**할 때 사용한다.) 대신,
`Checked Exception`을 처리할 때에는 다소 복잡하다. Publisher의 연산자(예를 들면 map) 내부에서 
**`Checked Exception`이 발생한다면 `try-catch` 구문 등을 사용하여 `Runtime Exception`으로 변환**해야한다.  

`Exceptions.propagate`를 사용하면 `Checked Exception`을 `Runtime Exception`으로 감쌀 수 있다.
### src/main/java/study/practice/Part07Errors.java
```java
public class Part07Errors {
  
  // TODO Return a Mono<User> containing User.SAUL when an error occurs in the input Mono, else do not change the input Mono.
  Mono<User> betterCallSaulForBogusMono(Mono<User> mono) {
    return mono.onErrorReturn(IllegalStateException.class, User.SAUL);
  }

  // TODO Return a Flux<User> containing User.SAUL and User.JESSE when an error occurs in the input Flux, else do not change the input Flux.
  Flux<User> betterCallSaulAndJesseForBogusFlux(Flux<User> flux) {
    return flux.onErrorResume(fallback -> Flux.just(User.SAUL, User.JESSE));
  }

  // TODO Implement a method that capitalizes each user of the incoming flux using the
  // #capitalizeUser method and emits an error containing a GetOutOfHereException error
  Flux<User> capitalizeMany(Flux<User> flux) {
    return flux.map(user -> {
      try {
        return capitalizeUser(user);
      } catch (GetOutOfHereException e) {
        throw Exceptions.propagate(e);
      }
    });
  }

  User capitalizeUser(User user) throws GetOutOfHereException {
    if (user.equals(User.SAUL)) {
      throw new GetOutOfHereException();
    }
    return new User(user.getUsername(), user.getFirstname(), user.getLastname());
  }

  protected final class GetOutOfHereException extends Exception {
    private static final long serialVersionUID = 0L;
  }

}
```
## #8 Other Operations
Reactor는 많은 연산자들이 있으므로 
[reference guide](https://projectreactor.io/docs/core/release/reference/index.html#which-operator)에서 
찾아보면 된다. 여기서는 몇개의 유용한 연산자만 다룬다.
### 메소드 정리
- `zip`: **두개 이상의 소스를 합친다.** 다양한 형태로 제공된다.
- `firstWithValue`: 여러 Publisher 소스들 중 하나를 먼저 골라 전달할 수 있다.
- `Mono<T> ignoreElements()`: `OnNext`: 신호를 무시한다.
- `Mono<Void> then()`: 해당 Mono에 완료 신호를 보내고 `Mono<Void>`를 리턴한다.
- `Mono<T> justOrEmpty(@Nullable T data)`: 인자(data)가 null이면 `Mono<Void>`를 리턴하고, not null이면 Mono.just(data)를 리턴한다.
- `Mono<T> switchIfEmpty(Mono<? extends T> alternate)`: 해당 Mono가 null이면 인자로 받은 Mono(alternate)를 리턴하고, not null이면 해당 Mono  리턴한다.
- `Mono<List<T>> collectList()`: `Flux<T>`를 `Mono<List<T>>`로 변환한다.
### 예제: src/main/java/study/practice/Part08OtherOperations.java
```java
public class Part08OtherOperations {
  
  // TODO Create a Flux of user from Flux of username, firstname and lastname.
  Flux<User> userFluxFromStringFlux(Flux<String> usernameFlux, Flux<String> firstnameFlux, Flux<String> lastnameFlux) {
    return Flux.zip(usernameFlux, firstnameFlux, lastnameFlux).map(tuple -> new User(tuple.getT1(),
        tuple.getT2(), tuple.getT3()));
  }
  
  // TODO Return the mono which returns its value faster
  Mono<User> useFastestMono(Mono<User> mono1, Mono<User> mono2) {
    return Mono.firstWithValue(mono1, mono2);
  }
  
  // TODO Return the flux which returns the first value faster
  Flux<User> useFastestFlux(Flux<User> flux1, Flux<User> flux2) {
    return Flux.firstWithValue(flux1, flux2);
  }
  
  // TODO Convert the input Flux<User> to a Mono<Void> that represents the complete signal of the flux
  Mono<Void> fluxCompletion(Flux<User> flux) {
    return flux.ignoreElements().then();
  }
  
  // TODO Return a valid Mono of user for null input and non null input user (hint: Reactive Streams do not accept null values)
  Mono<User> nullAwareUserToMono(User user) {
    return Mono.justOrEmpty(user);
  }
  
  // TODO Return the same mono passed as input parameter, expect that it will emit User.SKYLER when empty
  Mono<User> emptyToSkyler(Mono<User> mono) {
    return mono.switchIfEmpty(Mono.just(User.SKYLER));
  }
  
  // TODO Convert the input Flux<User> to a Mono<List<User>> containing list of collected flux values
  Mono<List<User>> fluxCollection(Flux<User> flux) {
    return flux.collectList();
  }

}
```
## 9. Adapt
Reactor는 같은 Reative Streams의 구현체인 RxJava 3와 상호작용이 가능하다.
그리고 Mono 클래스는 Java 8+에서 지원하는 CompletableFuture 클래스와 상호작용이 가능하다.
### 메소드 정리
- **Flux**
  - `static <T> Flux<T> from(Publisher<? extends T> source)`: Publisher(rxjava의 Flowable 같은)를 Flux 객체로 변환한다.
- **Flowable**
  - `static <T> Flowable<T> fromPublisher(Publisher<? extends T> publisher)`: Publisher(Flux 같은)를 Flowable 객체로 변환한다.
- **Observable**
  - `static <T> Observable<T> fromPublisher(Publisher<? extends T> publisher)`: Publisher를 Observable 객체로 변환한다.
  - `Flowable<T> toFlowable(BackpressureStrategy strategy)`: 해당 Observable(rxjava) 객체를 Flowable 객체로 변환한다. (Observable는 backpressure을 지원하지않아 strategy를 정의해줘야만 한다.)
- **Single**
  - `static <T> Single<T> fromPublisher(Publisher<? extends T> publisher)`: Publisher(Mono 같은)를 rxjava의 Single 객체로 변환한다.
- **Mono**
  - `static <T> Mono<T> from(Publisher<? extends T> source)`: Publisher(Single 같은)를 Mono객체로 변환한다.
  - `CompletableFuture<T> toFuture()`: 해당 Mono객체를 CompletableFuture 객체로 변환한다.
  - `static <T> Mono<T> fromFuture(CompletableFuture<? extends T> future)`: CompletableFuture를 Mono 객체로 변환한다.
### 예제: src/main/java/study/practice/Part09Adapt.java
```java
public class Part09Adapt {
  
  // TODO Adapt Flux to RxJava Flowable
  Flowable<User> fromFluxToFlowable(Flux<User> flux) {
    return Flowable.fromPublisher(flux);
  }

  // TODO Adapt RxJava Flowable to Flux
  Flux<User> fromFlowableToFlux(Flowable<User> flowable) {
    return Flux.from(flowable);
  }


  // TODO Adapt Flux to RxJava Observable
  Observable<User> fromFluxToObservable(Flux<User> flux) {
    return Observable.fromPublisher(flux);
  }

  // TODO Adapt RxJava Observable to Flux
  Flux<User> fromObservableToFlux(Observable<User> observable) {
    return Flux.from(observable.toFlowable(BackpressureStrategy.BUFFER));
  }


  // TODO Adapt Mono to RxJava Single
  Single<User> fromMonoToSingle(Mono<User> mono) {
    return Single.fromPublisher(mono);
  }

  // TODO Adapt RxJava Single to Mono
  Mono<User> fromSingleToMono(Single<User> single) {
    return Mono.from(single.toFlowable());
  }


  // TODO Adapt Mono to Java 8+ CompletableFuture
  CompletableFuture<User> fromMonoToCompletableFuture(Mono<User> mono) {
    return mono.toFuture();
  }

  // TODO Adapt Java 8+ CompletableFuture to Mono
  Mono<User> fromCompletableFutureToMono(CompletableFuture<User> future) {
    return Mono.fromFuture(future);
  }

}
```