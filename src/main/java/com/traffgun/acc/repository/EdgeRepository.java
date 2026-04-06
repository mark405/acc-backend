package com.traffgun.acc.repository;

import com.traffgun.acc.entity.Edge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EdgeRepository extends JpaRepository<Edge, Long> {
    List<Edge> findAllByProjectId(Long projectId);
}