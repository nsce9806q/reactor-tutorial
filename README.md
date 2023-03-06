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
