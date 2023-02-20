package dev.nelson.reactor;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

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
	public void crateAFlux_fromStream(){
		
	}

}
