package com.carol.cardschallenge.domain;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Result {

    private List<Player> winners;
    private String result;
}
