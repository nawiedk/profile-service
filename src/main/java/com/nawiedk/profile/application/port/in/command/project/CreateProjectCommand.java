package com.nawiedk.profile.application.port.in.command.project;

import com.nawiedk.profile.domain.model.Skill;

import java.util.Set;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public record CreateProjectCommand(
        String name,
        String description,
        Set<Skill> technologies,
        Set<String> urls
) {
    public CreateProjectCommand(String name, String description, Set<Skill> technologies, Set<String> urls) {
        this.name = name;
        this.description = description;
        this.technologies = technologies;
        this.urls = urls;
        validate(this);
    }
}
