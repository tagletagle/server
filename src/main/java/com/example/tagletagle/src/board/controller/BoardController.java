package com.example.tagletagle.src.board.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.tagletagle.src.board.service.BoardService;

@RestController
public class BoardController {

	private final BoardService boardService;

	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}

}
