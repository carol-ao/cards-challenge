package com.carol.cardschallenge.controller;

import com.carol.cardschallenge.domain.Result;
import com.carol.cardschallenge.exceptions.DeckOfCardsAPIErrorException;
import com.carol.cardschallenge.service.DeckofCardsAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("cards-challenge")
public class CardsChallengeController {

    @Autowired
    private DeckofCardsAPIService deckofCardsAPIService;

    @GetMapping
    public ResponseEntity<Result> run() throws DeckOfCardsAPIErrorException {
        return ResponseEntity.ok().body(deckofCardsAPIService.run());
    }

    @GetMapping("/winners-so-far")
    public ResponseEntity<List<Result>> getResultsHistory(){
        return ResponseEntity.ok().body(deckofCardsAPIService.getResultsHistory());
    }
}
