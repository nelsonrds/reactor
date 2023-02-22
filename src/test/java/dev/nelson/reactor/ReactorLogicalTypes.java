package dev.nelson.reactor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class ReactorLogicalTypes {

    @Test
    public void all() {
        Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");

        Mono<Boolean> hasAMono = animalFlux.all(a -> a.contains("a"));

        StepVerifier.create(hasAMono).expectNext(true).verifyComplete();

        Mono<Boolean> hasKMono = animalFlux.all(a -> a.contains("k"));

        StepVerifier.create(hasKMono).expectNext(false).verifyComplete();

    }

    @Test
    public void any(){
        Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");

        Mono<Boolean> hasTMono = animalFlux.any(a -> a.contains("t"));

        StepVerifier.create(hasTMono).expectNext(true).verifyComplete();

        Mono<Boolean> hasZMono = animalFlux.all(a -> a.contains("z"));

        StepVerifier.create(hasZMono).expectNext(false).verifyComplete();
    }
}
