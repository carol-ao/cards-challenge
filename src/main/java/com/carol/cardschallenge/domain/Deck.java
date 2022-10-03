package com.carol.cardschallenge.domain;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Deck {

    private String deck_id;
    private Integer remaining;
}
