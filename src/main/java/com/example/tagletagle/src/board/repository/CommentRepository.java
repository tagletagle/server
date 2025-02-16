package com.example.tagletagle.src.board.repository;

import com.example.tagletagle.src.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

}
