package com.nawiedk.profile.domain.model;

import com.nawiedk.profile.domain.util.ProfileStatus;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public class Profile {

    @Valid
    private final ProfileId profileId;

    @Getter
    @NotBlank
    @Size(min = 2, max = 100)
    private final String name;


    @Getter
    @Size(max = 2000)
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
    private final String githubUrl;

    @Getter
    private final String linkedInUrl;

    @Getter
    @NotNull
    private final ProfileStatus profileStatus;

    @Getter
    private final List<@Valid Skill> skills;

    @Getter
    private final List<@Valid Project> projects;

    @Getter
    private final UserId userId;

    public Profile(
            ProfileId profileId,
            String name,
            String bio,
            String location,
            String role,
            String imageUrl,
            String githubUrl,
            String linkedInUrl,
            ProfileStatus profileStatus,
            List<Skill> skills,
            List<Project> projects,
            UserId userId

    ) {
        this.profileId = profileId;
        this.name = name;
        this.bio = bio;
        this.location = location;
        this.role = role;
        this.imageUrl = imageUrl;
        this.githubUrl = githubUrl;
        this.linkedInUrl = linkedInUrl;
        this.profileStatus = profileStatus;
        this.skills = skills;
        this.projects = projects;
        this.userId = userId;
        validate(this);
    }

    public record ProfileId(Long profileId) {
        public ProfileId(@NotNull Long profileId) {
            if (profileId <= 0)
                throw new ConstraintViolationException("Profile ID must be a positive number.", new HashSet<>());
            this.profileId = profileId;
            validate(this);
        }
    }

    public record UserId(Long userId) {
        public UserId(@NotNull Long userId) {
            if (userId <= 0)
                throw new ConstraintViolationException("User ID must be a positive number.", new HashSet<>());
            this.userId = userId;
            validate(this);
        }
    }


    public static Profile profileWithId(ProfileId profileId, String name, String bio, String location, String role, String imageUrl, String githubUrl, String linkedInUrl, ProfileStatus profileStatus, List<Skill> skills, List<Project> projects, UserId userId) {
        return new Profile(profileId, name, bio, location, role, imageUrl, githubUrl, linkedInUrl, profileStatus, skills, projects, userId);
    }

    public static Profile profileWithoutId(String name, String bio, String location, String role, String imageUrl, String githubUrl, String linkedInUrl, ProfileStatus profileStatus, List<Skill> skills, List<Project> projects, UserId userId) {
        return new Profile(null, name, bio, location, role, imageUrl, githubUrl, linkedInUrl, profileStatus, skills, projects, userId);
    }

    public Optional<Profile.ProfileId> getProfileId() {
        return Optional.ofNullable(this.profileId);
    }

    public Optional<Profile.UserId> getUserId() {
        return Optional.ofNullable(this.userId);
    }

    public String getProfileStatusAsString() {
        return this.profileStatus.toString();
    }

}
