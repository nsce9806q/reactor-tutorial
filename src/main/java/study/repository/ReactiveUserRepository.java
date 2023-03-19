package study.repository;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import study.domain.User;


/**
 * ReactiveRepository의 구현체가 없어 추론하여 구현함.
 * @author ChangEn Yea
 */
public class ReactiveUserRepository implements ReactiveRepository<User> {

  @Override
  public Mono<Void> save(Publisher<User> publisher) {
    return Mono.empty();
  }

//  Part11BlockingToReative 2번 예제에서 repository.save하는 부분에서 argument가 Publisher가 아닌 User로 오버로딩함.
  public Mono<Void> save(User user){
    return Mono.empty();
  }

  @Override
  public Mono<User> findFirst() {
    return Mono.just(User.SKYLER);
  }

  @Override
  public Flux<User> findAll() {
    return Flux.just(User.SKYLER, User.JESSE, User.WALTER, User.SAUL);
  }

  @Override
  public Mono<User> findById(String id) {
    return null;
  }
}
