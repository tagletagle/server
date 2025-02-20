package com.example.tagletagle.src.board.dto;

import java.util.List;

import com.example.tagletagle.utils.TagUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "게시글 정보를 담은 DTO")
@NoArgsConstructor
public class PostInfoDTO {

	private Long postId;
	private String title;
	private String url;
	private String imageUrl;
	private Long commentCount;
	private Long likeCount;
	private Long scrapCount;

	private Long authorId;
	private String authorNickname;
	private String authorProfileImgUrl;

	private Boolean isLike;
	private Boolean isScrap;

	private List<TagInfoDTO> tagInfoDTOList;

	public PostInfoDTO(Long postId, String title, String url, String imageUrl,Long commentCount, Long likeCount, Long scrapCount,
		Long authorId, String authorNickname, String authorProfileImgUrl, Long isLike, Long isScrap, String tagIds, String tagNames) {
		this.postId = postId;
		this.title = title;
		this.url = url;
		this.imageUrl = imageUrl;
		this.commentCount = commentCount;
		this.likeCount = likeCount;
		this.scrapCount = scrapCount;
		this.authorId = authorId;
		this.authorNickname = authorNickname;
		this.authorProfileImgUrl = authorProfileImgUrl;
		System.out.println(isLike);
		this.isLike = (isLike == null || isLike == 0) ? Boolean.FALSE : Boolean.TRUE;
		System.out.println(isScrap);
		this.isScrap = (isScrap == null || isScrap == 0) ? Boolean.FALSE : Boolean.TRUE;
		this.tagInfoDTOList = TagUtils.parseTags(tagIds, tagNames);
	}
}
