package com.carol.cardschallenge.service;

import com.carol.cardschallenge.domain.*;
import com.google.common.collect.Lists;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class DeckofCardsAPIService {

    private static final int CARDS_PER_HAND = 5;
    private static final int NUMBER_OF_PLAYERS = 5;

    public Result run() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Application");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        Deck deck = restTemplate.exchange("https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1", HttpMethod.GET, entity, Deck.class).getBody();
        DrawnCards drawnCards = restTemplate.exchange("https://deckofcardsapi.com/api/deck/" + deck.getDeck_id() + "/draw/?count=" + CARDS_PER_HAND * NUMBER_OF_PLAYERS,
                HttpMethod.GET, entity, DrawnCards.class).getBody();

        validateDrawnCards(drawnCards);

        final int[] maxScore = {Integer.MIN_VALUE};
        List<Player> players = getPlayersHands(drawnCards, maxScore);

        return getResult(players, maxScore[0]);

        // NEXT STEPS:
        // exceptions and handler
        // make tests
        // re-add cards to deck after game and reuse deck?
        // maybe use h2 database

    }


    private void validateDrawnCards(DrawnCards drawnCards) {

        //refactor to validate drawnCards method
        if (Objects.isNull(drawnCards) || !drawnCards.isSuccess()) {
            //throw exception
        }

        if (Objects.isNull(drawnCards.getCards()) || drawnCards.getCards().size() != CARDS_PER_HAND * NUMBER_OF_PLAYERS) {
            //throw exception
        }
    }

    private List<Player> getPlayersHands(DrawnCards drawnCards, final int[] maxScore) {

        List<Player> players = new ArrayList<>();
        List<List<Card>> allDrawnCards = Lists.partition(drawnCards.getCards(), NUMBER_OF_PLAYERS);

        allDrawnCards.forEach(subset -> {
            Player newPlayer = Player.builder()
                    .cards(subset)
                    .build();

            newPlayer.calculateScore();
            if (newPlayer.getScore() > maxScore[0]) {
                maxScore[0] = newPlayer.getScore();
            }
            players.add(newPlayer);
        });
        return players;
    }

    private Result getResult(List<Player> players, int maxScore) {

        List<Player> winners = players.stream().filter(player -> player.getScore() == maxScore).collect(Collectors.toList());
        String result;
        if (winners.size() > 1) {
            result = "The result is a draw between " + winners.size() + " players with the score of "+ winners.get(0).getScore()+".";
            for (int i = 0; i < winners.size(); ++i) {
                result += " Player " + i + "'s cards: " + winners.get(i).getCards()+";";
            }
        } else {
            result = "We have a winner with a score of " + winners.get(0).getScore() + " points. The winners cards are: " + winners.get(0).getCards();
        }
        return Result.builder()
                .result(result)
                .build();
    }

}
