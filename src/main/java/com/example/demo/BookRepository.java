package com.example.demo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BookRepository extends ReactiveCrudRepository<BookEntity, Long> {}
