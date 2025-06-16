package com.nawiedk.profile.application.port.in.command.project;

import com.nawiedk.profile.domain.model.Skill;
import com.nawiedk.profile.domain.util.ProjectStatus;

import java.util.Set;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public record UpdateProjectCommand(
        Long projectId,
        String name,
        String previewText,
        String description,
        Set<Skill> technologies,
        String githubUrl,
        ProjectStatus projectStatus
) {
    public UpdateProjectCommand(Long projectId, String name, String previewText, String description, Set<Skill> technologies, String githubUrl, ProjectStatus projectStatus) {
        this.projectId = projectId;
        this.name = name;
        this.previewText = previewText;
        this.description = description;
        this.technologies = technologies;
        this.githubUrl = githubUrl;
        this.projectStatus = projectStatus;
        validate(this);
    }
}
