package com.traffgun.acc.repository;

import com.traffgun.acc.entity.Board;
import com.traffgun.acc.entity.Operation;
import com.traffgun.acc.model.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Set;

public interface OperationRepository extends JpaRepository<Operation, Long>, JpaSpecificationExecutor<Operation> {
    @Modifying
    @Query("DELETE FROM Operation o WHERE o.board.id = :boardId")
    void deleteByBoardId(@Param("boardId") Long boardId);

    @Modifying
    @Query("DELETE FROM Operation o WHERE o.category.id = :categoryId")
    void deleteByCategoryId(@Param("categoryId") Long categoryId);

    Set<Operation> findAllByDateBetweenAndBoardAndOperationType(Instant dateAfter, Instant dateBefore, Board board, OperationType operationType);
}
