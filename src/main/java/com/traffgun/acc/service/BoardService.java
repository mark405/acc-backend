package com.traffgun.acc.service;

import com.traffgun.acc.dto.board.CreateBoardRequest;
import com.traffgun.acc.dto.board.UpdateBoardRequest;
import com.traffgun.acc.entity.Board;
import com.traffgun.acc.entity.Project;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.model.LevelType;
import com.traffgun.acc.model.OperationType;
import com.traffgun.acc.repository.BoardRepository;
import com.traffgun.acc.repository.CategoryRepository;
import com.traffgun.acc.repository.OperationRepository;
import com.traffgun.acc.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final OperationRepository operationRepository;
    private final CategoryRepository categoryRepository;
    private final ProjectRepository projectRepository;

    public BoardService(BoardRepository boardRepository, OperationRepository operationRepository, CategoryRepository categoryRepository, ProjectRepository projectRepository) {
        this.boardRepository = boardRepository;
        this.operationRepository = operationRepository;
        this.categoryRepository = categoryRepository;
        this.projectRepository = projectRepository;

        if (!boardRepository.existsByLevelTypeAndOperationType(LevelType.MAIN, OperationType.EXPENSE)) {
            boardRepository.save(Board.builder().name("Головна").levelType(LevelType.MAIN).operationType(OperationType.EXPENSE).build());
        }

        if (!boardRepository.existsByLevelTypeAndOperationType(LevelType.MAIN, OperationType.INCOME)) {
            boardRepository.save(Board.builder().name("Головна").levelType(LevelType.MAIN).operationType(OperationType.INCOME).build());
        }
    }

    @Transactional(readOnly = true)
    public Board findMainBoard(OperationType operationType) {
        return boardRepository.findByLevelTypeAndOperationType(LevelType.MAIN, operationType);
    }

    @Transactional(readOnly = true)
    public List<Board> findAll(Long projectId, OperationType type) {
        return boardRepository.findAllByProject_IdAndOperationType(projectId, type);
    }

    @Transactional
    public Board update(Board board, UpdateBoardRequest request) {
        board.setName(request.getName());
        return boardRepository.save(board);
    }

    @Transactional
    public void deleteById(Long id) {
        operationRepository.deleteByBoardId(id);
        categoryRepository.deleteByBoardId(id);
        boardRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Board> findById(Long id) {
        return boardRepository.findById(id);
    }

    @Transactional
    public Board create(CreateBoardRequest request) {
        Project project = projectRepository.findById(request.getProjectId()).orElseThrow(() -> new EntityNotFoundException(request.getProjectId()));
        return boardRepository.save(Board.builder()
                .name(request.getName())
                .levelType(LevelType.CUSTOM)
                .operationType(request.getType())
                .project(project)
                .build()
        );
    }
}
