package com.example.tagletagle.src.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "태그 정보를 담은 DTO")
@AllArgsConstructor
@NoArgsConstructor
public class TagInfoDTO {

	private Long tagId;
	private String tagName;

}
