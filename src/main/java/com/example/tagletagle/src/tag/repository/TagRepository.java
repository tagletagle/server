package com.example.tagletagle.src.tag.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.tagletagle.src.tag.entity.TagEntity;

public interface TagRepository extends JpaRepository<TagEntity, Long> {


}
