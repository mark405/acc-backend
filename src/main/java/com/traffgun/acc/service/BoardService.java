package com.traffgun.acc.service;

import com.traffgun.acc.dto.board.CreateBoardRequest;
import com.traffgun.acc.dto.board.UpdateBoardRequest;
import com.traffgun.acc.entity.Board;
import com.traffgun.acc.model.LevelType;
import com.traffgun.acc.model.OperationType;
import com.traffgun.acc.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;

        if (!boardRepository.existsByLevelTypeAndOperationType(LevelType.MAIN, OperationType.EXPENSE)) {
            boardRepository.save(Board.builder().name("Головна").levelType(LevelType.MAIN).operationType(OperationType.EXPENSE).build());
        }

        if (!boardRepository.existsByLevelTypeAndOperationType(LevelType.MAIN, OperationType.INCOME)) {
            boardRepository.save(Board.builder().name("Головна").levelType(LevelType.MAIN).operationType(OperationType.INCOME).build());
        }
    }

    @Transactional(readOnly = true)
    public List<Board> findAll(OperationType type) {
        return boardRepository.findAllByOperationType(type);
    }

    @Transactional
    public Board update(Board board, UpdateBoardRequest request) {
        board.setName(request.getName());
        return boardRepository.save(board);
    }

    @Transactional
    public void deleteById(Long id) {
        boardRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Board> findById(Long id) {
        return boardRepository.findById(id);
    }

    @Transactional
    public Board create(CreateBoardRequest request) {
        return boardRepository.save(Board.builder()
                .name(request.getName())
                .levelType(LevelType.CUSTOM)
                .operationType(request.getType())
                .build()
        );
    }
}
