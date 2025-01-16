package com.example.tagletagle.src.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "댓글 정보를 담은 DTO")
@NoArgsConstructor
public class CommentInfoDTO {

	private Long commentId;
	private String contents;

	private Long authorId;
	private String authorNickname;
	private String authorProfileImgUrl;

	public CommentInfoDTO(Long commentId, String contents, Long authorId, String authorNickname,
		String authorProfileImgUrl) {
		this.commentId = commentId;
		this.contents = contents;
		this.authorId = authorId;
		this.authorNickname = authorNickname;
		this.authorProfileImgUrl = authorProfileImgUrl;
	}
}
