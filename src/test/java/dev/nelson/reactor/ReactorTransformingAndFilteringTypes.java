package dev.nelson.reactor;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@SpringBootTest
public class ReactorTransformingAndFilteringTypes {

    @Test
    public void skipAFew() {
        Flux<String> fluxTest = Flux.just("one", "two", "skip a few", "ninety nine", "one hundred").skip(3);

        StepVerifier.create(fluxTest).expectNext("ninety nine").expectNext("one hundred").verifyComplete();
    }

    @Test
    public void skipAFewSeconds() {
        Flux<String> fluxTest = Flux.just("one", "two", "skip a few", "ninety nine", "one hundred")
                .delayElements(Duration.ofSeconds(1)).skip(Duration.ofSeconds(4));

        StepVerifier.create(fluxTest).expectNext("ninety nine").expectNext("one hundred").verifyComplete();

    }

    @Test
    public void takeAFew() {
        Flux<String> fluxTest = Flux.just("YellowStone", "Yosemite", "Grand Canyon", "Zion", "Acadia").take(3);

        StepVerifier.create(fluxTest).expectNext("YellowStone", "Yosemite", "Grand Canyon").verifyComplete();
    }

    @Test
    public void takeAFewDuration() {
        Flux<String> fluxTest = Flux.just("YellowStone", "Yosemite", "Grand Canyon", "Zion", "Acadia")
                .delayElements(Duration.ofSeconds(1)).take(Duration.ofMillis(3500));
        StepVerifier.create(fluxTest).expectNext("YellowStone", "Yosemite", "Grand Canyon").verifyComplete();
    }

    @Test
    public void filterRemoveWithSpace() {
        Flux<String> fluxTest = Flux.just("YellowStone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
                .filter(f -> !f.contains(" "));
        StepVerifier.create(fluxTest).expectNext("YellowStone", "Yosemite", "Zion").verifyComplete();
    }

    @Test
    public void distinctValues() {
        Flux<String> fluxTest = Flux.just("dog", "cat", "bird", "dog", "bird", "anteater").distinct();

        StepVerifier.create(fluxTest).expectNext("dog").expectNext("cat").expectNext("bird").expectNext("anteater")
                .verifyComplete();
    }

    @Test
    public void mappingValues() {
        Flux<Player> fluxTest = Flux.just("Michael Jordan", "Scottie Pippen").map(t -> {
            String[] split = t.split(" ");
            return new Player(split[0], split[1]);
        });

        StepVerifier.create(fluxTest).expectNext(new Player("Michael", "Jordan"))
                .expectNext(new Player("Scottie", "Pippen")).verifyComplete();
    }

    @Data
    private static class Player {
        private final String firstName;
        private final String lastName;
    }

    @Test
    public void flatMappingValues() {
        Flux<Player> fluxTest = Flux.just("Michael Jordan", "Scottie Pippen").flatMap(n -> Mono.just(n).map(t -> {
            String[] split = t.split(" ");
            return new Player(split[0], split[1]);
        }).subscribeOn(Schedulers.parallel()));

        StepVerifier.create(fluxTest).expectNext(new Player("Michael", "Jordan"))
                .expectNext(new Player("Scottie", "Pippen")).verifyComplete();
    }

    @Test
    public void bufferedFlex() {
        Flux<String> fluxTest = Flux.just("apple", "orange", "strawberry", "kiwi", "banana");
        Flux<List<String>> flexListTest = fluxTest.buffer(3);

        StepVerifier.create(flexListTest).expectNext(Arrays.asList("apple", "orange", "strawberry"))
                .expectNext(Arrays.asList("kiwi", "banana")).verifyComplete();
    }

    @Test
    public void bufferAndFlatMap() {
        Flux.just("apple", "orange", "strawberry", "kiwi", "banana")
                .buffer(3)
                .flatMap(fruit -> Flux.fromIterable(fruit).map(item -> item.toUpperCase())
                        .subscribeOn(Schedulers.parallel()).log())
                .subscribe();
    }

    @Test
    public void collectionList() {
        Flux<String> fruitFlux = Flux.just("apple", "orange", "strawberry", "kiwi", "banana");

        Mono<List<String>> fruitFluxMono = fruitFlux.collectList();

        StepVerifier.create(fruitFluxMono)
                .expectNext(Arrays.asList("apple", "orange", "strawberry", "kiwi", "banana")).verifyComplete();

    }

    @Test
    public void collectMap() {
        Flux<String> fruitFlux = Flux.just("apple", "orange", "strawberry", "apple", "orange");

        Mono<Map<Character, String>> fruitMono = fruitFlux.collectMap((c) -> c.charAt(0));

        StepVerifier.create(fruitMono).expectNextMatches(map -> {
            return map.size() == 3 && map.get('a').equals("apple") && map.get('o').equals("orange")
                    && map.get('s').equals("strawberry");
        }).verifyComplete();
    }
}
