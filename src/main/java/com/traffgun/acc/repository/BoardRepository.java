package com.traffgun.acc.repository;

import com.traffgun.acc.entity.Board;
import com.traffgun.acc.model.LevelType;
import com.traffgun.acc.model.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    boolean existsByLevelTypeAndOperationType(LevelType levelType, OperationType operationType);

    List<Board> findAllByProject_IdAndOperationType(Long projectId, OperationType operationType);

    Board findByLevelTypeAndOperationTypeAndProject_Id(LevelType levelType, OperationType operationType, Long projectId);
}
