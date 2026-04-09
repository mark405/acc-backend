package com.traffgun.acc.repository;

import com.traffgun.acc.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT COALESCE(MAX(p.index), 0) FROM Project p")
    Integer findMaxIndex();
}
