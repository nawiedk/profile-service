package com.nawiedk.profile.application.port.in.command.profile;

import com.nawiedk.profile.domain.util.ProfileStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public record CreateProfileCommand(
        @NotBlank @Size(min = 2, max = 100) String name,
        @Size(max = 1000) String bio,
        @NotBlank String location,
        @NotBlank String role,
        @NotEmpty ProfileStatus profileStatus,
        @NotBlank String imageUrl
) {

    public CreateProfileCommand(String name, String bio, String location, String role, ProfileStatus profileStatus, String imageUrl) {
        this.name = name;
        this.bio = bio;
        this.location = location;
        this.role = role;
        this.imageUrl = imageUrl;
        this.profileStatus = profileStatus;
        validate(this);
    }
}
