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
    private boolean isPublic;

    @ElementCollection
    private List<String> skills;

    public ProfileEntity() {
    }

    public ProfileEntity(Long id, String name, String bio, String location, String role, List<String> skills, String imageUrl, boolean isPublic) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.location = location;
        this.role = role;
        this.skills = skills;
        this.imageUrl = imageUrl;
        this.isPublic = isPublic;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getBio() { return bio; }
    public String getLocation() { return location; }
    public String getRole() { return role; }
    public List<String> getSkills() { return skills; }
    public String getImageUrl() { return imageUrl; }
    public boolean isPublic() { return isPublic; }
}
