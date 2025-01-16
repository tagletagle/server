package com.example.tagletagle.src.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.tagletagle.src.board.dto.PostInfoDTO;
import com.example.tagletagle.src.board.entity.PostEntity;
import com.example.tagletagle.src.user.entity.UserEntity;

import jakarta.persistence.SqlResultSetMapping;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

	/*@Query(value = "SELECT p.id AS postId, p.title AS title, p.url AS url," +
		"p.comment_count AS commentCount, p.like_count AS likeCount, p.scrap_count AS scrapCount, "+
		"u.id AS authorId, u.nickname AS authorNickname, u.profile_img_url AS authorProfileImgUrl, " +
		"pl.id AS isLike, ps.id AS isScrap " +
		"FROM post p " +
		"INNER JOIN user u ON u.id = p.user_id " +
		"LEFT JOIN post_like pl ON pl.post_id = p.id AND pl.user_id = :loginUserId " +
		"LEFT JOIN post_scrap ps ON ps.post_id = p.id AND ps.user_id = :loginUserId " +
		"WHERE p.user_id = :authorId",
		nativeQuery = true)
	List<PostInfoDTO> findPostsByAuthorAndUser(
		@Param("authorId") Long authorId,
		@Param("loginUserId") Long loginUserId
	);*/


}
