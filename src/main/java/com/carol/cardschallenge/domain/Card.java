package com.carol.cardschallenge.domain;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Card {

    private String code;

    @Override
    public String toString() {
        return "\'"+ code + "\'";
    }
}
