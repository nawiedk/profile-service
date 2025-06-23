package com.nawiedk.profile.application.port.in.command.profile;

import com.nawiedk.profile.domain.util.ProfileStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public record CreateProfileCommand(
        @NotBlank @Size(min = 2, max = 100) String name,
        @Size(max = 2000) String bio,
        @NotBlank String location,
        @NotBlank String role,
        @NotBlank String imageUrl,
        String githubUrl,
        String linkedInUrl,
        @NotEmpty ProfileStatus profileStatus
) {

    public CreateProfileCommand(String name, String bio, String location, String role, String imageUrl, String githubUrl, String linkedInUrl, ProfileStatus profileStatus) {
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
