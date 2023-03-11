package study.practice;

//generic imports to help with simpler IDEs (ie tech.io)
import java.util.*;
import java.util.function.*;
import java.time.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import study.domain.User;

/**
 * Learn how to merge flux.
 *
 * @author Sebastien Deleuze
 */
public class Part05Merge {

//=============ㅁ===========================================================================

  // TODO Merge flux1 and flux2 values with interleave
  Flux<User> mergeFluxWithInterleave(Flux<User> flux1, Flux<User> flux2) {
    return flux1.mergeWith(flux2);
  }

//========================================================================================

  // TODO Merge flux1 and flux2 values with no interleave (flux1 values and then flux2 values)
  Flux<User> mergeFluxWithNoInterleave(Flux<User> flux1, Flux<User> flux2) {
    return flux1.concatWith(flux2);
  }

//========================================================================================

  // TODO Create a Flux containing the value of mono1 then the value of mono2
  Flux<User> createFluxFromMultipleMono(Mono<User> mono1, Mono<User> mono2) {
    return mono1.concatWith(mono2);
  }

}
