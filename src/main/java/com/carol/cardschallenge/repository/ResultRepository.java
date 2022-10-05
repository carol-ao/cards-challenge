package com.carol.cardschallenge.repository;

import com.carol.cardschallenge.domain.Result;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepository extends JpaRepository<Result,Long> {
}
