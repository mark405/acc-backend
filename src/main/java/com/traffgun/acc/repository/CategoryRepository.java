package com.traffgun.acc.repository;

import com.traffgun.acc.entity.Board;
import com.traffgun.acc.entity.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByBoard(Board board, Sort sort);

    @Modifying
    @Query("DELETE FROM Category o WHERE o.board.id = :id")
    void deleteByBoardId(Long id);
}
