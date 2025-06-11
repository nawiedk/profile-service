package com.nawiedk.profile.domain.model;

import com.nawiedk.profile.domain.util.ProfileStatus;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public class Profile {

    @Valid
    private final ProfileId profileId;

    @Getter
    @NotBlank
    @Size(min = 2, max = 100)
    private final String name;


    @Getter
    @Size(max = 1000)
    private final String bio;

    @Getter
    @NotBlank
    private final String location;

    @Getter
    @NotBlank
    private final String role;

    @Getter
    @NotBlank
    private final String imageUrl;

    @Getter
    @NotNull
    private final ProfileStatus profileStatus;

    @Getter
    private final Set<@Valid Skill> skills;

    @Getter
    private final Set<@Valid Project> projects;

    public Profile(
            ProfileId profileId,
            String name,
            String bio,
            String location,
            String role,
            String imageUrl,
            ProfileStatus profileStatus,
            Set<Skill> skills,
            Set<Project> projects
    ) {
        this.profileId = profileId;
        this.name = name;
        this.bio = bio;
        this.location = location;
        this.role = role;
        this.imageUrl = imageUrl;
        this.profileStatus = profileStatus;
        this.skills = skills;
        this.projects = projects;
        validate(this);
    }

    public record ProfileId(Long profileId) {
        public ProfileId(@NotNull Long profileId) {
            if (profileId == null)
                throw new ConstraintViolationException("Profile ID cannot be null", new HashSet<>());
            this.profileId = profileId;
            validate(this);
        }
    }

    public static Profile profileWithId(ProfileId profileId, String name, String bio, String location, String role, String imageUrl, ProfileStatus profileStatus, Set<Skill> skills, Set<Project> projects) {
        return new Profile(profileId, name, bio, location, role, imageUrl, profileStatus, skills, projects);
    }

    public static Profile profileWithoutId(String name, String bio, String location, String role, String imageUrl, ProfileStatus profileStatus, Set<Skill> skills, Set<Project> projects) {
        return new Profile(null, name, bio, location, role, imageUrl, profileStatus, skills, projects);
    }

    public Optional<Profile.ProfileId> getProfileId() {
        return Optional.ofNullable(this.profileId);
    }

    public String getProfileStatusAsString() {
        return this.profileStatus.toString();
    }

}
