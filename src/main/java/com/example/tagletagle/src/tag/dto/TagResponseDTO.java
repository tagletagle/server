package com.example.tagletagle.src.tag.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TagResponseDTO {
  private Long userId;
  private List<Long> interestTagId;
}
