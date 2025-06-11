package com.nawiedk.profile.application.port.in.command.project;

import jakarta.validation.constraints.NotNull;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public record GetProjectCommand(@NotNull Long projectId) {
    public GetProjectCommand(Long projectId) {
        this.projectId = projectId;
        validate(this);
    }
}
