package com.nawiedk.profile.application.port.in.command;

import jakarta.validation.constraints.NotNull;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public record DeleteProfileCommand(@NotNull Long id) {
    public DeleteProfileCommand(Long id) {
        this.id = id;
        validate(this);
    }
}
