package com.traffgun.acc.controller;

import com.traffgun.acc.dto.board.CreateBoardRequest;
import com.traffgun.acc.dto.board.BoardResponse;
import com.traffgun.acc.dto.board.UpdateBoardRequest;
import com.traffgun.acc.entity.Board;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.mapper.BoardMapper;
import com.traffgun.acc.model.OperationType;
import com.traffgun.acc.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardMapper boardMapper;

    @GetMapping
    public List<BoardResponse> getAllBoards(@RequestParam("type") OperationType type) {
        return boardService.findAll(type).stream().map(boardMapper::toDto).toList();
    }

    @PostMapping()
    public BoardResponse createBoard(@RequestBody @Valid CreateBoardRequest request) {
        Board updatedBoard = boardService.create(request);
        return boardMapper.toDto(updatedBoard);
    }

    @PutMapping("/{id}")
    public BoardResponse updateBoard(@PathVariable("id") Long id, @RequestBody @Valid UpdateBoardRequest request) {
        Board board = boardService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        Board updatedBoard = boardService.update(board, request);
        return boardMapper.toDto(updatedBoard);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteBoard(@PathVariable("id") Long id) {
        boardService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}