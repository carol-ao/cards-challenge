package com.carol.cardschallenge.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionsHandler {

  @ControllerAdvice
  public class RestExceptionHandler {
    @ExceptionHandler(DeckOfCardsAPIErrorException.class)
    public ResponseEntity<StandardError> deckOfCardsAPIError(
            Exception e, HttpServletRequest httpServletRequest) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(
                      StandardError.builder()
                              .timestamp(LocalDateTime.now())
                              .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                              .message(e.getMessage())
                              .path(httpServletRequest.getRequestURI())
                              .build());
    }
  }
}
