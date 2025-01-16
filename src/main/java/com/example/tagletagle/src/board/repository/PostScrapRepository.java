package com.example.tagletagle.src.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.tagletagle.src.board.entity.PostEntity;
import com.example.tagletagle.src.board.entity.PostScrapEntity;
import com.example.tagletagle.src.user.entity.UserEntity;

public interface PostScrapRepository extends JpaRepository<PostScrapEntity, Long> {

	Boolean existsByPostAndUser(PostEntity post, UserEntity user);

	@Modifying
	@Transactional
	@Query("DELETE FROM PostScrapEntity ps WHERE ps.user = :user AND ps.post =:post")
	int deletePostScrapEntityByUserAndPost(@Param("user") UserEntity user, @Param("post") PostEntity post);
}
