package com.carol.cardschallenge.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class StandardError {

  private LocalDateTime timestamp;
  private Integer status;
  private String message;
  private String path;
}
