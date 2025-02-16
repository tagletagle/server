package com.example.tagletagle.src.tag.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor  // 기본 생성자 추가

public class TagDTO {
    private Long id;
    private String name;
}
