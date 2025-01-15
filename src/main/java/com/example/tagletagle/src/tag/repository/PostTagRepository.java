package com.example.tagletagle.src.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tagletagle.src.tag.entity.PostTagEntity;

public interface PostTagRepository extends JpaRepository<PostTagEntity, Long> {
}
