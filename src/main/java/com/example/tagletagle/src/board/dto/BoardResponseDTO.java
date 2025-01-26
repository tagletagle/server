package com.example.tagletagle.src.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoardResponseDTO {
    private Long id;
    private String title;
    private String url;
    private Long likeCount;
    private Long commentCount;
}
