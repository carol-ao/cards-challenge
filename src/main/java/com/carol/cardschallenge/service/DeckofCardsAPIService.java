package com.carol.cardschallenge.service;

import com.carol.cardschallenge.domain.*;
import com.carol.cardschallenge.exceptions.DeckOfCardsAPIErrorException;
import com.carol.cardschallenge.repository.DeckRepository;
import com.carol.cardschallenge.repository.ResultRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class DeckofCardsAPIService {

    private static final int DECK_SIZE = 52;
    private static final int CARDS_PER_HAND = 5;
    private static final int NUMBER_OF_PLAYERS = 4;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private DeckRepository deckRepository;

    private HttpEntity<String> entity;

    private RestTemplate restTemplate;

    public Result run() throws DeckOfCardsAPIErrorException {

        configureRestTemplate();

        Deck deck = getDeck();

        deck = shuffleDeck(deck);

        DrawnCards drawnCards = drawCards(deck);

        final int[] maxScore = {Integer.MIN_VALUE};

        List<Player> players = getPlayersHands(drawnCards, maxScore);

        Result result = getResult(players, maxScore[0]);

        resultRepository.save(result);

        return result;
    }

    // NEXT STEPS:
    // exceptions handler
    // make tests


    private void configureRestTemplate() {
        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Application");
        entity = new HttpEntity<>(headers);
    }

    private Deck getDeck() throws DeckOfCardsAPIErrorException {
        Optional<Deck> savedDeck = deckRepository.findTopByOrderByIdDesc();
        Deck deck = null;
        if (!savedDeck.isPresent()) {
            try {
                deck = restTemplate.exchange("https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1", HttpMethod.GET, entity, Deck.class).getBody();
            } catch (Exception e) {
                throw new DeckOfCardsAPIErrorException("Could neither retrieve deck nor get a new one to play : error when Calling API.");
            }

            deckRepository.save(deck);
        } else {
            deck = savedDeck.get();
        }
        return deck;
    }

    private Deck shuffleDeck(Deck deck) throws DeckOfCardsAPIErrorException {
        ReturnOrShuffleResponse shuffleResponse =
                restTemplate.exchange("https://deckofcardsapi.com/api/deck/" + deck.getDeck_id() + "/shuffle/?remaining=true",
                        HttpMethod.GET, entity, ReturnOrShuffleResponse.class).getBody();

        if (!shuffleResponse.isSuccess()) {
            throw new DeckOfCardsAPIErrorException("Could not shuffle cards before drawing: API returned payload but could not shuffle the cards..");
        }

        return deck;
    }

    private DrawnCards drawCards(Deck deck) throws DeckOfCardsAPIErrorException {

        DrawnCards drawnCards = null;
        try {
           drawnCards= restTemplate.exchange("https://deckofcardsapi.com/api/deck/" + deck.getDeck_id() + "/draw/?count=" + CARDS_PER_HAND * NUMBER_OF_PLAYERS,
                    HttpMethod.GET, entity, DrawnCards.class).getBody();
        }catch(Exception e){
            throw new DeckOfCardsAPIErrorException("Could not draw cards : Error when calling API.");
        }

        if (Objects.isNull(drawnCards) || !drawnCards.isSuccess() || Objects.isNull(drawnCards.getCards()) || drawnCards.getCards().size() != CARDS_PER_HAND * NUMBER_OF_PLAYERS) {
            throw new DeckOfCardsAPIErrorException("Failed to draw cards : API returned payload but could not draw the cards.");
        }

        deck.setRemaining(drawnCards.getRemaining());

        if (drawnCards.getRemaining() < (DECK_SIZE - (CARDS_PER_HAND * NUMBER_OF_PLAYERS))) {

            //returning cards in api if not enough cards remain
            ReturnOrShuffleResponse returnResponse = restTemplate.exchange("https://deckofcardsapi.com/api/deck/" + deck.getDeck_id() + "/return/", HttpMethod.GET, entity, ReturnOrShuffleResponse.class).getBody();

            if (!returnResponse.isSuccess()) {
                throw new DeckOfCardsAPIErrorException("Could not return cards  : API returned payload but could not return the cards to the deck.");
            }

            //restoring deck size
            deck.setRemaining(DECK_SIZE);
        }

        deckRepository.save(deck);

        return drawnCards;
    }

    private List<Player> getPlayersHands(DrawnCards drawnCards, final int[] maxScore) {

        List<Player> players = new ArrayList<>();
        List<List<Card>> allDrawnCards = Lists.partition(drawnCards.getCards(), CARDS_PER_HAND);

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
            result = "The result is a draw between " + winners.size() + " players with the score of " + winners.get(0).getScore() + ".";
            for (int i = 0; i < winners.size(); ++i) {
                result += " Player " + i + "'s cards: " + winners.get(i).getCards() + ";";
            }
        } else {
            result = "We have a winner with a score of " + winners.get(0).getScore() + " points. The winners cards are: " + winners.get(0).getCards();
        }
        return Result.builder()
                .result(result)
                .build();
    }

    public List<Result> getResultsHistory() {
        return resultRepository.findAll();  // change to order by desc
    }
}
