package com.traffgun.acc.repository;

import com.traffgun.acc.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NodeRepository extends JpaRepository<Node, Long> {
    List<Node> findAllByProjectId(Long projectId);
}