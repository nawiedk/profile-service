package com.nawiedk.profile.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Getter
public class Profile {
    private final ProfileId profileId;
    private final String name;
    private final String bio;
    private final String location;
    private final String role;
    private final String imageUrl;
    private final ProfileStatus profileStatus;
    private final Set<String> skills;

    public record ProfileId(Long id){}


    public static Profile profileWithId(ProfileId profileId, String name, String bio, String location, String role, Set<String> skills, String imageUrl, ProfileStatus profileStatus) {
        return new Profile(profileId, name, bio, location, role, imageUrl, profileStatus, skills);
    }

    public static Profile profileWithoutId(String name, String bio, String location, String role, Set<String> skills, String imageUrl, ProfileStatus profileStatus) {
        return new Profile(null, name, bio, location, role, imageUrl, profileStatus, skills);
    }

    public Optional<Profile.ProfileId> getId() {
        return Optional.ofNullable(this.profileId);
    }

    public String getProfileStatusAsString(){
        return this.profileStatus.toString();
    }

}
