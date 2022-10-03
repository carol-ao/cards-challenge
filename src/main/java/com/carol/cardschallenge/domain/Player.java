package com.carol.cardschallenge.domain;

import lombok.*;

import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Player {

    private int id;
    private List<Card> cards;
}
