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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final OperationRepository operationRepository;
    private final CategoryRepository categoryRepository;
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public Board findMainBoard(OperationType operationType, Long projectId) {
        return boardRepository.findByLevelTypeAndOperationTypeAndProject_Id(LevelType.MAIN, operationType, projectId);
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
