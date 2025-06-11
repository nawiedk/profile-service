package com.nawiedk.profile.application.port.in.command.project;

import com.nawiedk.profile.domain.model.Skill;

import java.util.Set;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public record UpdateProjectCommand(
        Long projectId,
        String name,
        String description,
        Set<Skill> technologies,
        Set<String> urls
) {
    public UpdateProjectCommand(Long projectId, String name, String description, Set<Skill> technologies, Set<String> urls) {
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.technologies = technologies;
        this.urls = urls;
        validate(this);
    }
}
