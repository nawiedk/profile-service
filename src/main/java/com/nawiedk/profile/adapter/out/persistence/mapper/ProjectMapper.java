package com.nawiedk.profile.adapter.out.persistence.mapper;

import com.nawiedk.profile.adapter.out.persistence.jpaentity.ProjectJpaEntity;
import com.nawiedk.profile.domain.model.Project;
import com.nawiedk.profile.domain.model.Project.ProjectId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final SkillMapper skillMapper;

    public Project mapToDomainEntity(ProjectJpaEntity entity) {
        return Project.projectWithId(
                new ProjectId(entity.getProjectId()),
                entity.getName(),
                entity.getPreviewText(),
                entity.getDescription(),
                entity.getTechnologies().stream().map(skillMapper::mapToDomainEntity).collect(Collectors.toList()),
                entity.getGithubUrl(),
                entity.getProjectStatus()
        );
    }

    public ProjectJpaEntity mapToJpaEntity(Project project) {
        ProjectJpaEntity entity = new ProjectJpaEntity();
        entity.setProjectId(project.getProjectId().map(ProjectId::projectId).orElse(null));
        entity.setName(project.getName());
        entity.setPreviewText(project.getPreviewText());
        entity.setDescription(project.getDescription());
        entity.setTechnologies(project.getTechnologies().stream().map(skillMapper::mapToJpaEntity).collect(Collectors.toList()));
        entity.setGithubUrl(project.getGithubUrl());
        entity.setProjectStatus(project.getProjectStatus());
        return entity;
    }

}
