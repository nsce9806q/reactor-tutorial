package study.practice;

//generic imports to help with simpler IDEs (ie tech.io)
import java.util.*;
import java.util.concurrent.Flow.Subscriber;
import java.util.function.*;
import java.time.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import study.domain.User;
import study.repository.ReactiveUserRepository;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * Learn how to call blocking code from Reactive one with adapted concurrency strategy for
 * blocking code that produces or receives data.
 *
 * For those who know RxJava:
 *  - RxJava subscribeOn = Reactor subscribeOn
 *  - RxJava observeOn = Reactor publishOn
 *  - RxJava Schedulers.io <==> Reactor Schedulers.elastic
 *
 * @author Sebastien Deleuze
 * @see Flux#subscribeOn(Scheduler)
 * @see Flux#publishOn(Scheduler)
 * @see Schedulers
 */
public class Part11BlockingToReactive {

//========================================================================================

  // TODO Create a Flux for reading all users from the blocking repository deferred until the flux is subscribed, and run it with a bounded elastic scheduler
  Flux<User> blockingRepositoryToFlux(ReactiveUserRepository repository) {
    return Flux.defer(() -> repository.findAll().subscribeOn(Schedulers.boundedElastic()));
  }

//========================================================================================

  // TODO Insert users contained in the Flux parameter in the blocking repository using a bounded elastic scheduler and return a Mono<Void> that signal the end of the operation
  Mono<Void> fluxToBlockingRepository(Flux<User> flux, ReactiveUserRepository repository) {
    return flux.publishOn(Schedulers.boundedElastic()).doOnNext(repository::save).then();
  }

}
