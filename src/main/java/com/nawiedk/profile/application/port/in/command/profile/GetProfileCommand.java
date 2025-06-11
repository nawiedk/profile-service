package com.nawiedk.profile.application.port.in.command.profile;

import jakarta.validation.constraints.NotNull;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public record GetProfileCommand(@NotNull Long profileId) {
    public GetProfileCommand(Long profileId) {
        this.profileId = profileId;
        validate(this);
    }
}
