package com.carol.cardschallenge.domain;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pile implements Serializable {

    private boolean success;
    private String deck_id;
    private List<Card> cards;
    private int remaining;

}
