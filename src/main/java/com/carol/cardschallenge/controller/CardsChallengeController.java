package com.carol.cardschallenge.controller;

import com.carol.cardschallenge.service.DeckofCardsAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("cards-challenge")
public class CardsChallengeController {

    @Autowired
    private DeckofCardsAPIService deckofCardsAPIService;

    @GetMapping
    public ResponseEntity<Void> run(){
        deckofCardsAPIService.run();
        return ResponseEntity.ok().build(); //change to return Result
    }
}
