package com.example.tagletagle.src.board.service;

import org.springframework.stereotype.Service;

import com.example.tagletagle.src.board.repository.BoardRepository;

@Service
public class BoardService {

	private final BoardRepository boardRepository;

	public BoardService(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

}
