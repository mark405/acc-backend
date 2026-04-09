package com.traffgun.acc.mapper;

import com.traffgun.acc.dto.project.ProjectResponse;
import com.traffgun.acc.entity.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectMapper {
    private final UserMapper userMapper;

    public ProjectResponse toDto(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                userMapper.toUserDto(project.getCreatedBy()),
                project.getComment(),
                project.getIndex()
        );
    }
}
