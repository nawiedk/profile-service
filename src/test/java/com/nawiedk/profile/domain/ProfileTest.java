package com.nawiedk.profile.domain;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {

    private final Skill java = new Skill(new Skill.SkillId(1L), "Java", "Backend", 8);
    private final Skill react = new Skill(new Skill.SkillId(2L), "React", "Frontend", 6);
    private final Skill docker = new Skill(new Skill.SkillId(3L), "Docker", "DevOps", 7);

    @Test
    void shouldCreateValidProfile() {
        Profile profile = Profile.profileWithoutId(
                "test dummy",
                "i like software dev, especially backend",
                "Düsseldorf",
                "DevOps",
                ProfileStatus.ACTIVE, "image.png", Set.of(java, react, docker)
        );

        assertEquals("test dummy", profile.getName());
        assertEquals("i like software dev, especially backend", profile.getBio());
        assertEquals("Düsseldorf", profile.getLocation());
        assertEquals("DevOps", profile.getRole());
        assertEquals("image.png", profile.getImageUrl());
        assertEquals(ProfileStatus.ACTIVE, profile.getProfileStatus());
        assertEquals(3, profile.getSkills().size());
        assertTrue(profile.getProfileId().isEmpty(), "ProfileId should be empty for profileWithoutId");
        assertEquals("ACTIVE", profile.getProfileStatusAsString());
    }

    @Test
    void shouldCreateValidProfileWithId() {
        Profile.ProfileId profileId = new Profile.ProfileId(1L);
        Profile profile = Profile.profileWithId(
                profileId,
                "test dummy with id",
                "bio for profile with id",
                "Berlin",
                "Frontend Developer",
                "new_image.jpg",
                ProfileStatus.INACTIVE,
                Set.of(react)
        );

        assertEquals(profileId, profile.getProfileId().orElse(null), "ProfileId should be present");
        assertEquals("test dummy with id", profile.getName());
        assertEquals("INACTIVE", profile.getProfileStatusAsString());
    }

    @Test
    void shouldFailToCreateProfileWithNullName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Profile.profileWithoutId(
                null,
                "i like software dev, especially backend",
                "Düsseldorf",
                "DevOps",
                ProfileStatus.ACTIVE, "image.png", Set.of(java)
        ));
        assertTrue(exception.getMessage().toLowerCase().contains("name"));
    }

    @Test
    void shouldFailToCreateProfileWithBlankName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Profile.profileWithoutId(
                " ",
                "i like software dev, especially backend",
                "Düsseldorf",
                "DevOps",
                ProfileStatus.ACTIVE, "image.png", Set.of(java)
        ));
        assertTrue(exception.getMessage().toLowerCase().contains("name"));
    }

    @Test
    void shouldFailToCreateProfileWithNameToShort() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Profile.profileWithoutId(
                "A",
                "i like software dev, especially backend",
                "Düsseldorf",
                "DevOps",
                ProfileStatus.ACTIVE, "image.png", Set.of(java)
        ));
        assertTrue(exception.getMessage().toLowerCase().contains("name"));
        assertTrue(exception.getMessage().toLowerCase().contains("size"));
    }

    @Test
    void shouldFailToCreateProfileWithNameToLong() {
        String longName = "a".repeat(101);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Profile.profileWithoutId(
                longName,
                "i like software dev, especially backend",
                "Düsseldorf",
                "DevOps",
                ProfileStatus.ACTIVE, "image.png", Set.of(java)
        ));
        assertTrue(exception.getMessage().toLowerCase().contains("name"));
        assertTrue(exception.getMessage().toLowerCase().contains("size"));
    }

    @Test
    void shouldFailToCreateProfileWithBioToLong() {
        String longBio = "a".repeat(1001);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Profile.profileWithoutId(
                "test dummy",
                longBio,
                "Düsseldorf",
                "DevOps",
                ProfileStatus.ACTIVE, "image.png", Set.of(java)
        ));
        assertTrue(exception.getMessage().toLowerCase().contains("bio"));
        assertTrue(exception.getMessage().toLowerCase().contains("size"));
    }


    @Test
    void shouldFailToCreateProfileWithBlankLocation() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Profile.profileWithoutId(
                "test dummy",
                "bio",
                " ",
                "DevOps",
                ProfileStatus.ACTIVE, "image.png", Set.of(java)
        ));
        assertTrue(exception.getMessage().toLowerCase().contains("location"));
    }

    @Test
    void shouldFailToCreateProfileWithBlankRole() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Profile.profileWithoutId(
                "test dummy",
                "bio",
                "Düsseldorf",
                " ",
                ProfileStatus.ACTIVE, "image.png", Set.of(java)
        ));
        assertTrue(exception.getMessage().toLowerCase().contains("role"));
    }

    @Test
    void shouldFailToCreateProfileWithBlankImageUrl() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Profile.profileWithoutId(
                "test dummy",
                "bio",
                "Düsseldorf",
                "DevOps",
                ProfileStatus.ACTIVE,
                " ",
                Set.of(java)
        ));
        assertTrue(exception.getMessage().toLowerCase().contains("imageurl"));
    }

    @Test
    void shouldFailToCreateProfileWithNullProfileStatus() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Profile.profileWithoutId(
                "test dummy",
                "bio",
                "Düsseldorf",
                "DevOps",
                null,
                "image.png",
                Set.of(java)
        ));
        assertTrue(exception.getMessage().toLowerCase().contains("profilestatus"));
    }


    @Test
    void shouldFailToCreateProfileWithEmptySkillSet() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Profile.profileWithoutId(
                "test dummy",
                "i like software dev, especially backend",
                "Düsseldorf",
                "DevOps",
                ProfileStatus.ACTIVE, "image.png", Set.of()
        ));
        assertTrue(exception.getMessage().toLowerCase().contains("skills"));
    }

    @Test
    void shouldFailToCreateProfileWithNullSkillSet() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Profile.profileWithoutId(
                "test dummy",
                "i like software dev, especially backend",
                "Düsseldorf",
                "DevOps",
                ProfileStatus.ACTIVE, "image.png", null
        ));
        assertTrue(exception.getMessage().toLowerCase().contains("skills"));
    }

    @Test
    void profileIdRecordShouldValidateId() {
        Profile.ProfileId validId = new Profile.ProfileId(1L);
        assertEquals(1L, validId.id());
        Profile.ProfileId nullId = new Profile.ProfileId(null);
        assertNull(nullId.id());
    }
}