package com.nawiedk.profile.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Getter
public class Profile {
    private final ProfileId profileId;
    @NotBlank
    private final String name;
    private final String bio;
    private final String location;
    private final String role;
    private final List<String> skills;
    private final String imageUrl;
    private final boolean profileStatus;

    public record ProfileId(Long id){}


    public static Profile profileWithId(ProfileId profileId, String name, String bio, String location, String role, List<String> skills, String imageUrl, boolean profileStatus) {
        return new Profile(profileId, name, bio, location, role, skills, imageUrl, profileStatus);
    }

    public static Profile profileWithoutId(String name, String bio, String location, String role, List<String> skills, String imageUrl, boolean profileStatus) {
        return new Profile(null, name, bio, location, role, skills, imageUrl, profileStatus);
    }

    public Optional<Profile.ProfileId> getId() {
        return Optional.ofNullable(this.profileId);
    }

}
