package com.example.tagletagle.src.board.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "게시글 작성을 위한 DTO")
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostDTO {

	@Schema(description = "게시글 제목")
	private String title;

	@Schema(description = "url")
	private String url;

	@Schema(description = "내용")
	@NotEmpty
	private String contents;

	@Schema(description = "게시글에 관련된 태그 목록(리스트 형태로 받아야합니다)")
	@NotEmpty
	private List<Long> tagIdList;


}
