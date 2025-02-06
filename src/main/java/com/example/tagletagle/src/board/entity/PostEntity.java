package com.example.tagletagle.src.board.entity;

import org.hibernate.annotations.DynamicInsert;

import com.example.tagletagle.base.BaseEntity;
import com.example.tagletagle.src.board.dto.CreatePostDTO;
import com.example.tagletagle.src.user.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "post")
@NoArgsConstructor
@DynamicInsert
public class PostEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column
	private String title;

	@Column
	private String url;

	@Column
	private String author;

	@Column(columnDefinition = "TEXT")
	private String image;

	@Column(name = "comment_count", nullable = false)
	private Long commentCount;

	@Column(name = "like_count", nullable = false)
	private Long likeCount;

	@Column(name = "scrap_count", nullable = false)
	private Long scrapCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	public PostEntity(CreatePostDTO createPostDTO, UserEntity user){
		this.title = createPostDTO.getTitle();
		this.url = createPostDTO.getUrl();
		this.commentCount = 0L;
		this.likeCount = 0L;
		this.scrapCount = 0L;
		this.user = user;
	}

	public PostEntity(CreatePostDTO createPostDTO, UserEntity user, String title ,String image, String author){
		this.title = title;
		this.url = createPostDTO.getUrl();
		this.image = image;
		this.author = author;
		this.commentCount = 0L;
		this.likeCount = 0L;
		this.scrapCount = 0L;
		this.user = user;
	}

	public PostEntity(CreatePostDTO createPostDTO, UserEntity user, String image, String author){
		this.title = createPostDTO.getTitle();
		this.url = createPostDTO.getUrl();
		this.image = image;
		this.author = author;
		this.commentCount = 0L;
		this.likeCount = 0L;
		this.scrapCount = 0L;
		this.user = user;
	}


}
