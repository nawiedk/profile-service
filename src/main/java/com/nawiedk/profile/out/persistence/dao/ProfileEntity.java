package com.nawiedk.profile.out.persistence.dao;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "profiles")
public class ProfileEntity {

    @Id
    private Long id;

    private String name;
    private String bio;
    private String location;
    private String role;
    private String imageUrl;
    private boolean profileStatus;

    @ElementCollection
    private List<String> skills;

    public ProfileEntity() {
    }

    public ProfileEntity(Long id, String name, String bio, String location, String role,
                         List<String> skills, String imageUrl, boolean profileStatus) {
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

    public void setName(String name) { this.name = name; }
    public void setBio(String bio) { this.bio = bio; }
    public void setLocation(String location) { this.location = location; }
    public void setRole(String role) { this.role = role; }
    public void setSkills(List<String> skills) { this.skills = skills; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setProfileStatus(boolean profileStatus) { this.profileStatus = profileStatus; }
}