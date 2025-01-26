package com.example.tagletagle.src.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tagletagle.src.user.entity.RefreshEntity;

import jakarta.transaction.Transactional;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

	Boolean existsByRefresh(String refresh);

	@Transactional
	int deleteByRefresh(String refresh);

}
