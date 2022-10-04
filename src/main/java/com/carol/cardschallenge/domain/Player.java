package com.carol.cardschallenge.domain;

import com.carol.cardschallenge.Utils;
import lombok.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Player{
    private List<Card> cards;

    private int score;

    public void calculateScore() {
        if (Objects.isNull(cards)) {
            //throw exception
        }
        score = 0;

        cards.forEach(card -> {
            score += Utils.convertCodeToNumericValue(card.getCode());
        });
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return  "{ "+cards +
                " }";
    }
}
