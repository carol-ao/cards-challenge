package com.carol.cardschallenge.service;

import com.carol.cardschallenge.domain.Deck;
import com.carol.cardschallenge.domain.Pile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class DeckofCardsAPIService {

    private static final int CARDS_PER_HAND = 5;
    private static final int NUMBER_OF_PLAYERS = 5;

    public void run() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Application");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        Deck deck = restTemplate.exchange("https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1", HttpMethod.GET, entity, Deck.class).getBody();
        Pile pile = restTemplate.exchange("https://deckofcardsapi.com/api/deck/" + deck.getDeck_id() + "/draw/?count=" + CARDS_PER_HAND * NUMBER_OF_PLAYERS,
                HttpMethod.GET, entity, Pile.class).getBody();

        // NEXT STEPS:
        // calculate each pile sum based on first character of card code ( maybe create enum to store value)
        // check who the winner is. put winner in list of winners
        // build result message depending on number of winners. Return all winners in case of a draw
        // refactor and make tests
        // add assertions and exception handler

    }

}
