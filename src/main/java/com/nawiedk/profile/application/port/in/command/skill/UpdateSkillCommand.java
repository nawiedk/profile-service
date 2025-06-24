package com.nawiedk.profile.application.port.in.command.skill;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public record UpdateSkillCommand(
        @NotNull Long skillId,
        @NotBlank String name,
        @NotBlank String category
) {
    public UpdateSkillCommand(Long skillId, String name, String category) {
        this.skillId = skillId;
        this.name = name;
        this.category = category;
        validate(this);
    }
}
