package study.practice;

//generic imports to help with simpler IDEs (ie tech.io)
import java.util.*;
import java.util.function.*;
import java.time.*;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import study.domain.User;
import study.repository.ReactiveRepository;
import study.repository.ReactiveUserRepository;

/**
 * Learn how to control the demand.
 *
 * @author Sebastien Deleuze
 */
public class Part06Request {

  ReactiveUserRepository repository = new ReactiveUserRepository();

//========================================================================================

  // TODO Create a StepVerifier that initially requests all values and expect 4 values to be received
  StepVerifier requestAllExpectFour(Flux<User> flux) {
    return StepVerifier.create(flux, 4).expectNextCount(4).expectComplete();
  }

//========================================================================================

  // TODO Create a StepVerifier that initially requests 1 value and expects User.SKYLER then requests another value and expects User.JESSE then stops verifying by cancelling the source
  StepVerifier requestOneExpectSkylerThenRequestOneExpectJesse(Flux<User> flux) {
    return StepVerifier.create(flux)
        .thenRequest(1).expectNext(User.SKYLER)
        .thenRequest(1).expectNext(User.JESSE)
        .thenCancel();
  }

//========================================================================================

  // TODO Return a Flux with all users stored in the repository that prints automatically logs for all Reactive Streams signals
  Flux<User> fluxWithLog() {
    return repository.findAll().log();
  }

//========================================================================================

  // TODO Return a Flux with all users stored in the repository that prints "Starring:" at first, "firstname lastname" for all values and "The end!" on complete
  Flux<User> fluxWithDoOnPrintln() {
    return repository.findAll()
        .doFirst(() -> System.out.println("Starring:"))
        .doOnNext(user -> System.out.println(user.getFirstname() + " " + user.getLastname()))
        .doOnComplete(() -> System.out.println("The end!"));
  }

}

