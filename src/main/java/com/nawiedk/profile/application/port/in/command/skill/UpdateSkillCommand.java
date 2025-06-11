package com.nawiedk.profile.application.port.in.command.skill;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public record UpdateSkillCommand(
        @NotNull Long skillId,
        @NotBlank String name,
        @Min(0) @Max(10) int proficiencyLevel
) {
    public UpdateSkillCommand(Long skillId, String name, int proficiencyLevel) {
        this.skillId = skillId;
        this.name = name;
        this.proficiencyLevel = proficiencyLevel;
        validate(this);
    }
}
