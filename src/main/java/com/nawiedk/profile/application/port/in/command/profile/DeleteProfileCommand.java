package com.nawiedk.profile.application.port.in.command.profile;

import jakarta.validation.constraints.NotNull;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public record DeleteProfileCommand(@NotNull Long profileId) {
    public DeleteProfileCommand(Long profileId) {
        this.profileId = profileId;
        validate(this);
    }
}
