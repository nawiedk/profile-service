package com.nawiedk.profile.domain;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileTest {

    @Test
    void shouldCreateValidProfileWithId() {
        Profile profile = Profile.profileWithId(
                new Profile.ProfileId(1L),
                "John Doe",
                "Software Engineer with passion",
                "London, UK",
                "Senior Developer",
                "https://example.com/image.jpg",
                ProfileStatus.ACTIVE,
                createValidSkillSet()
        );

        assertThat(profile.getProfileId()).isPresent();
        assertThat(profile.getProfileId().get().id()).isEqualTo(1L);
        assertThat(profile.getName()).isEqualTo("John Doe");
        assertThat(profile.getBio()).isEqualTo("Software Engineer with passion");
        assertThat(profile.getLocation()).isEqualTo("London, UK");
        assertThat(profile.getRole()).isEqualTo("Senior Developer");
        assertThat(profile.getImageUrl()).isEqualTo("https://example.com/image.jpg");
        assertThat(profile.getProfileStatus()).isEqualTo(ProfileStatus.ACTIVE);
        assertThat(profile.getSkills()).hasSize(1);
    }

    @Test
    void shouldCreateValidProfileWithoutId() {
        Profile profile = Profile.profileWithoutId(
                "John Doe",
                "Software Engineer with passion",
                "London, UK",
                "Senior Developer",
                "https://example.com/image.jpg",
                ProfileStatus.ACTIVE,
                createValidSkillSet()
        );

        assertThat(profile.getProfileId()).isEmpty();
        assertThat(profile.getName()).isEqualTo("John Doe");
    }

    @Test
    void shouldThrowExceptionWhenNameIsTooShort() {
        assertThatThrownBy(() -> Profile.profileWithoutId(
                "J",
                "Bio",
                "London, UK",
                "Developer",
                "https://example.com/image.jpg",
                ProfileStatus.ACTIVE,
                createValidSkillSet()
        )).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldThrowExceptionWhenNameIsTooLong() {
        String tooLongName = "a".repeat(101);
        assertThatThrownBy(() -> Profile.profileWithoutId(
                tooLongName,
                "Bio",
                "London, UK",
                "Developer",
                "https://example.com/image.jpg",
                ProfileStatus.ACTIVE,
                createValidSkillSet()
        )).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldThrowExceptionWhenBioIsTooLong() {
        String tooLongBio = "a".repeat(1001);
        assertThatThrownBy(() -> Profile.profileWithoutId(
                "John Doe",
                tooLongBio,
                "London, UK",
                "Developer",
                "https://example.com/image.jpg",
                ProfileStatus.ACTIVE,
                createValidSkillSet()
        )).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldThrowExceptionWhenLocationIsBlank() {
        assertThatThrownBy(() -> Profile.profileWithoutId(
                "John Doe",
                "Bio",
                "",
                "Developer",
                "https://example.com/image.jpg",
                ProfileStatus.ACTIVE,
                createValidSkillSet()
        )).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldThrowExceptionWhenRoleIsBlank() {
        assertThatThrownBy(() -> Profile.profileWithoutId(
                "John Doe",
                "Bio",
                "London, UK",
                "",
                "https://example.com/image.jpg",
                ProfileStatus.ACTIVE,
                createValidSkillSet()
        )).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldThrowExceptionWhenImageUrlIsBlank() {
        assertThatThrownBy(() -> Profile.profileWithoutId(
                "John Doe",
                "Bio",
                "London, UK",
                "Developer",
                "",
                ProfileStatus.ACTIVE,
                createValidSkillSet()
        )).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldThrowExceptionWhenSkillSetIsEmpty() {
        assertThatThrownBy(() -> Profile.profileWithoutId(
                "John Doe",
                "Bio",
                "London, UK",
                "Developer",
                "https://example.com/image.jpg",
                ProfileStatus.ACTIVE,
                new HashSet<>()
        )).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldThrowExceptionWhenProfileStatusIsNull() {
        assertThatThrownBy(() -> Profile.profileWithoutId(
                "John Doe",
                "Bio",
                "London, UK",
                "Developer",
                "https://example.com/image.jpg",
                null,
                createValidSkillSet()
        )).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldReturnProfileStatusAsString() {
        Profile profile = Profile.profileWithoutId(
                "John Doe",
                "Bio",
                "London, UK",
                "Developer",
                "https://example.com/image.jpg",
                ProfileStatus.ACTIVE,
                createValidSkillSet()
        );

        assertThat(profile.getProfileStatusAsString()).isEqualTo("ACTIVE");
    }

    private Set<Skill> createValidSkillSet() {
        Set<Skill> skills = new HashSet<>();
        skills.add(Skill.skillWithoutId("Java", "Programming", 8));
        return skills;
    }
}
