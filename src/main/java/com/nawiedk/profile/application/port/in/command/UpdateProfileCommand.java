package com.nawiedk.profile.application.port.in.command;

import com.nawiedk.profile.domain.ProfileStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Set;

import static com.nawiedk.profile.validation.Validation.validate;

public record UpdateProfileCommand(
        @NotNull Long id,
        @NotBlank @Size(min = 2, max = 100) String name,
        @Size(max = 1000) String bio,
        @NotBlank String location,
        @NotBlank String role,
        @NotEmpty Set<@NotBlank String> skills,
        @NotBlank String imageUrl,
        ProfileStatus profileStatus) {
    public UpdateProfileCommand(Long id, String name, String bio, String location, String role, Set<String> skills, String imageUrl, ProfileStatus profileStatus){
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.location = location;
        this.role = role;
        this.skills = skills;
        this.imageUrl = imageUrl;
        this.profileStatus = profileStatus;
        validate(this);
    }
}
