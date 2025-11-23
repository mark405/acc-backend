package com.traffgun.acc.repository;

import com.traffgun.acc.entity.History;
import com.traffgun.acc.entity.Operation;
import com.traffgun.acc.entity.User;
import com.traffgun.acc.model.history.HistoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    Page<History> findByType(HistoryType type, Pageable pageable);

    Page<History> findByUserInAndType(List<User> users, HistoryType type, Pageable pageable);

    Page<History> findByUserIn(List<User> users, Pageable pageable);
}
