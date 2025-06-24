package com.nawiedk.profile.application.port.in.command.skill;

import jakarta.validation.constraints.NotBlank;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public record CreateSkillCommand(
        @NotBlank String name,
        @NotBlank String category
) {
    public CreateSkillCommand(String name, String category) {
        this.name = name;
        this.category = category;
        validate(this);
    }
}
