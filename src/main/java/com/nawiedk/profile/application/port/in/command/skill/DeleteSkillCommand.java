package com.nawiedk.profile.application.port.in.command.skill;

import jakarta.validation.constraints.NotNull;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public record DeleteSkillCommand(@NotNull Long skillId) {
    public DeleteSkillCommand(Long skillId) {
        this.skillId = skillId;
        validate(this);
    }
}
