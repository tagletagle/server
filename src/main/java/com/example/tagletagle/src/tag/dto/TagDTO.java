package com.example.tagletagle.src.tag.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class TagDTO {
    private Long id;
    private String name;

    public TagDTO(Long id, String name) {  // 생성자로 값 할당
        this.id = id;
        this.name = name;
    }
}
