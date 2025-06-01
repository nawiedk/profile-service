package com.nawiedk.profile.application.port.in.command;

import com.nawiedk.profile.domain.model.ProfileStatusEnum;
import com.nawiedk.profile.domain.model.Skill;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public record CreateProfileCommand(
        @NotBlank @Size(min = 2, max = 100) String name,
        @Size(max = 1000) String bio,
        @NotBlank String location,
        @NotBlank String role,
        ProfileStatusEnum profileStatusEnum,
        @NotBlank String imageUrl,
        @NotEmpty Set<@Valid Skill> skills
) {

    public CreateProfileCommand(String name, String bio, String location, String role, ProfileStatusEnum profileStatusEnum, String imageUrl, Set<Skill> skills) {
        this.name = name;
        this.bio = bio;
        this.location = location;
        this.role = role;
        this.skills = skills;
        this.imageUrl = imageUrl;
        this.profileStatusEnum = profileStatusEnum;
        validate(this);
    }
}
