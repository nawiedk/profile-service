package com.nawiedk.profile.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Profile {
    private final Long id;
    private final String name;
    private final String bio;
    private final String location;
    private final String role;
    private final List<String> skills;
    private final String imageUrl;
    private final boolean profileStatus;

    public Profile(Long id, String name, String bio, String location, String role, List<String> skills, String imageUrl, boolean profileStatus) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.location = location;
        this.role = role;
        this.skills = skills;
        this.imageUrl = imageUrl;
        this.profileStatus = profileStatus;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getBio() { return bio; }
    public String getLocation() { return location; }
    public String getRole() { return role; }
    public List<String> getSkills() { return skills; }
    public String getImageUrl() { return imageUrl; }
    public boolean getProfileStatus() { return profileStatus; }
}
