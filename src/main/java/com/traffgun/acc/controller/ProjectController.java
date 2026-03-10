package com.traffgun.acc.controller;

import com.traffgun.acc.dto.project.CreateProjectRequest;
import com.traffgun.acc.dto.project.ProjectResponse;
import com.traffgun.acc.dto.project.UpdateProjectRequest;
import com.traffgun.acc.entity.Project;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.mapper.ProjectMapper;
import com.traffgun.acc.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ProjectResponse getProjectById(@PathVariable Long id) {
        Project project = projectService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        return projectMapper.toDto(project);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ProjectResponse createProject(@RequestBody CreateProjectRequest request) throws IllegalAccessException {
        Project project = projectService.create(request);
        return projectMapper.toDto(project);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProjectResponse updateProject(@PathVariable Long id, @RequestBody UpdateProjectRequest request) {
        Project project = projectService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        Project updatedProject = projectService.update(project, request);
        return projectMapper.toDto(updatedProject);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProjectResponse> getAllProjects() throws IllegalAccessException {
        return projectService.findAll().stream().map(projectMapper::toDto).toList();
    }
}
