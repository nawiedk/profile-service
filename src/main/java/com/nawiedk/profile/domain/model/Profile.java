package com.nawiedk.profile.domain.model;

import com.nawiedk.profile.domain.util.ProfileStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

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

    public Profile(
            ProfileId profileId,
            String name,
            String bio,
            String location,
            String role,
            String imageUrl,
            ProfileStatus profileStatus,
            Set<Skill> skills
    ) {
        this.profileId = profileId;
        this.name = name;
        this.bio = bio;
        this.location = location;
        this.role = role;
        this.imageUrl = imageUrl;
        this.profileStatus = profileStatus;
        this.skills = skills;
        validate(this);
    }

    public record ProfileId(Long id) {
        public ProfileId(Long id) {
            this.id = id;
            validate(this);
        }
    }

    public static Profile profileWithId(ProfileId profileId, String name, String bio, String location, String role, String imageUrl, ProfileStatus profileStatus, Set<Skill> skills) {
        return new Profile(profileId, name, bio, location, role, imageUrl, profileStatus, skills);
    }

    public static Profile profileWithoutId(String name, String bio, String location, String role, String imageUrl, ProfileStatus profileStatus, Set<Skill> skills) {
        return new Profile(null, name, bio, location, role, imageUrl, profileStatus, skills);
    }

    public Optional<Profile.ProfileId> getProfileId() {
        return Optional.ofNullable(this.profileId);
    }

    public String getProfileStatusAsString() {
        return this.profileStatus.toString();
    }

}
