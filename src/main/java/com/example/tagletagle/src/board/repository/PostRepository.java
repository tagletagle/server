package com.example.tagletagle.src.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tagletagle.src.board.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Long> {


}
