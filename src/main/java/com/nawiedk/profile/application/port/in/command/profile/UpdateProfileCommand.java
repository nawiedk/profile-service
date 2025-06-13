package com.nawiedk.profile.application.port.in.command.profile;

import com.nawiedk.profile.domain.util.ProfileStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public record UpdateProfileCommand(
        @NotNull Long profileId,
        @NotBlank @Size(min = 2, max = 100) String name,
        @Size(max = 1000) String bio,
        @NotBlank String location,
        @NotBlank String role,
        String linkedInUrl, @NotBlank String imageUrl, String githubUrl, @NotEmpty ProfileStatus profileStatus
) {
    public UpdateProfileCommand(Long profileId, String name, String bio, String location, String role, String linkedInUrl, String imageUrl, String githubUrl, ProfileStatus profileStatus) {
        this.profileId = profileId;
        this.name = name;
        this.bio = bio;
        this.location = location;
        this.role = role;
        this.imageUrl = imageUrl;
        this.githubUrl = githubUrl;
        this.linkedInUrl = linkedInUrl;
        this.profileStatus = profileStatus;
        validate(this);
    }
}
