package com.carol.cardschallenge.repository;

import com.carol.cardschallenge.domain.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeckRepository extends JpaRepository<Deck,Integer> {

    Optional<Deck> findTopByOrderByIdDesc();
}
