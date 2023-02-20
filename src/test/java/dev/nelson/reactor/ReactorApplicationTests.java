package dev.nelson.reactor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

@SpringBootTest
class ReactorApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void createAFlux_just() {
		Flux<String> fruitFlux = Flux.just("Apple", "Orange", "Grape", "Banana", "Strawberry");

		fruitFlux.subscribe(f -> System.out.println("Here's some fruit: " + f));

		StepVerifier.create(fruitFlux).expectNext("Apple").expectNext("Orange").expectNext("Grape").expectNext("Banana")
				.expectNext("Strawberry").verifyComplete();
	}

	@Test
	public void crateAFlux_fromArray() {
		String[] fruits = new String[] { "Apple", "Orange", "Grape", "Banana", "Strawberry" };

		Flux<String> fruitFlux = Flux.fromArray(fruits);

		StepVerifier.create(fruitFlux).expectNext("Apple").expectNext("Orange").expectNext("Grape").expectNext("Banana")
				.expectNext("Strawberry").verifyComplete();

	}

	@Test
	public void createAFlux_fromList() {
		List<String> fruits = new ArrayList<>();
		fruits.add("Apple");
		fruits.add("Orange");
		fruits.add("Grape");
		fruits.add("Banana");
		fruits.add("Strawberry");

		Flux<String> fruitFlux = Flux.fromIterable(fruits);

		StepVerifier.create(fruitFlux).expectNext("Apple").expectNext("Orange").expectNext("Grape").expectNext("Banana")
				.expectNext("Strawberry").verifyComplete();

	}

	@Test
	public void crateAFlux_fromStream() {
		Stream<String> fruits = Stream.of("Apple", "Orange", "Grape", "Banana", "Strawberry");

		Flux<String> fruitFlux = Flux.fromStream(fruits);

		StepVerifier.create(fruitFlux).expectNext("Apple").expectNext("Orange").expectNext("Grape").expectNext("Banana")
				.expectNext("Strawberry").verifyComplete();
	}

	@Test
	public void createAFlux_range() {
		Flux<Integer> intervalFlux = Flux.range(1, 5);

		StepVerifier.create(intervalFlux)
				.expectNext(1).expectNext(2).expectNext(3).expectNext(4).expectNext(5).verifyComplete();
	}

	@Test
	public void createAFlux_interval() {
		Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(1)).take(5);

		StepVerifier.create(intervalFlux)
				.expectNext(0L).expectNext(1L).expectNext(2L).expectNext(3L).expectNext(4L).verifyComplete();
	}

	@Test
	public void createAFlux_merge() {
		Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbossa").delayElements(Duration.ofMillis(500));
		Flux<String> foodFlux = Flux.just("Lasagna", "Lollipops", "Apples").delaySubscription(Duration.ofMillis(250))
				.delayElements(Duration.ofMillis(500));

		Flux<String> mergedFlux = characterFlux.mergeWith(foodFlux);

		StepVerifier.create(mergedFlux).expectNext("Garfield").expectNext("Lasagna").expectNext("Kojak")
				.expectNext("Lollipops").expectNext("Barbossa").expectNext("Apples").expectComplete();

		mergedFlux.subscribe((f) -> System.out.println("Here's the result: " + f));
	}

	@Test
	public void crateAFlux_zip() {
		Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbossa");
		Flux<String> foodFlux = Flux.just("Lasagna", "Lollipops", "Apples");

		Flux<Tuple2<String, String>> zipFlux = Flux.zip(characterFlux, foodFlux);

		StepVerifier.create(zipFlux)
				.expectNextMatches(p -> p.getT1().equals("Garfield") && p.getT2().equals("Lasagna"))
				.expectNextMatches(p -> p.getT1().equals("Kojak") && p.getT2().equals("Lollipops"))
				.expectNextMatches(p -> p.getT1().equals("Barbossa") && p.getT2().equals("Apples")).expectComplete();

	}

	@Test
	public void zipFluxesToObject() {
		Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbossa");
		Flux<String> foodFlux = Flux.just("Lasagna", "Lollipops", "Apples");

		Flux<String> zipFlux = Flux.zip(characterFlux, foodFlux, (c, z) -> c + " eats " + z);

		StepVerifier.create(zipFlux)
				.expectNext("Garfield eats Lasagna")
				.expectNext("Kojak eats Lollipops")
				.expectNext("Barbossa eats Apples").verifyComplete();

	}

	

}
