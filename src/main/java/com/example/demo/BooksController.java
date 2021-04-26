package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Slf4j
public class BooksController {

    private final BookRepository bookRepository;

    @PostMapping
    public Mono<Void> generateBooks() {
        log.info("Creating books...");
        return Flux.fromIterable(Stream.generate(() -> new BookEntity(UUID.randomUUID().toString(), UUID.randomUUID().toString()))
                .limit(100).collect(Collectors.toList()))
                .flatMap(bookRepository::save)
                .doOnComplete(() -> log.info("Books generated"))
                .then();
    }
}
