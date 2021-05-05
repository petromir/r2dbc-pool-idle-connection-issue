package com.example.demo;

import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.RequiredArgsConstructor;

@Table(value = "book")
@RequiredArgsConstructor
@ToString
public class BookEntity {

	@Id
	@Column(value = "id")
	private Long id;

	@Column(value = "title")
	private final String title;

	@Column(value = "author")
	private final String author;
}
