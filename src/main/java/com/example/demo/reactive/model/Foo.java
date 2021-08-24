package com.example.demo.reactive.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Foo {
    private Long id;
    private String name;
}
