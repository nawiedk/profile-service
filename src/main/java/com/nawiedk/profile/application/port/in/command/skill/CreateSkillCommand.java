package com.nawiedk.profile.application.port.in.command.skill;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public record CreateSkillCommand(
        @NotBlank String name,
        @Min(0) @Max(10) int proficiencyLevel
) {
    public CreateSkillCommand(String name, int proficiencyLevel) {
        this.name = name;
        this.proficiencyLevel = proficiencyLevel;
        validate(this);
    }
}
