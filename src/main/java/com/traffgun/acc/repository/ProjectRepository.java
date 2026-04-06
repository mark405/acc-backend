package com.traffgun.acc.repository;

import com.traffgun.acc.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByIdIn(Collection<Long> ids);
}
