package com.example.tagletagle.src.board.service;

import org.springframework.stereotype.Service;

import com.example.tagletagle.src.board.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;


}
