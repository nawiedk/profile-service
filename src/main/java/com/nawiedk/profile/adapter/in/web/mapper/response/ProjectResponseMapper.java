package com.nawiedk.profile.adapter.in.web.mapper.response;

import com.nawiedk.profile.domain.model.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProjectResponseMapper {
    private final SkillResponseMapper skillResponseMapper;

    public ProjectResponse mapToResponse(Project entity) {
        return new ProjectResponse(
                entity.getProjectId().map(Project.ProjectId::projectId).orElse(null),
                entity.getName(),
                entity.getPreviewText(),
                entity.getDescription(),
                entity.getTechnologies().stream().map(skillResponseMapper::mapToResponse).collect(Collectors.toList()),
                entity.getGithubUrl(),
                entity.getProjectStatusAsString()
        );
    }
}
