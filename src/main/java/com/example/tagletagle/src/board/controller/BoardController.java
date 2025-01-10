package com.example.tagletagle.src.board.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.tagletagle.src.board.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;


}
