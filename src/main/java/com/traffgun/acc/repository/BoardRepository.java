package com.traffgun.acc.repository;

import com.traffgun.acc.entity.Board;
import com.traffgun.acc.model.LevelType;
import com.traffgun.acc.model.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOperationType(OperationType operationType);

    boolean existsByLevelTypeAndOperationType(LevelType levelType, OperationType operationType);
}
