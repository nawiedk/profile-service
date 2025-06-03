package com.nawiedk.profile.application.port.in.command;

import com.nawiedk.profile.domain.model.Skill;
import com.nawiedk.profile.domain.util.ProfileStatus;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreateProfileCommandTest {

    private final Set<Skill> validSkills = Set.of(
            Skill.skillWithId(new Skill.SkillId(1L), "Java", "Programming", 8)
    );

    @Test
    void shouldCreateValidCommand() {
        CreateProfileCommand command = new CreateProfileCommand(
                "John Doe",
                "Test Bio",
                "Test Location",
                "Developer",
                ProfileStatus.ACTIVE,
                "http://example.com/image.jpg",
                validSkills
        );

        assertThat(command.name()).isEqualTo("John Doe");
        assertThat(command.bio()).isEqualTo("Test Bio");
        assertThat(command.location()).isEqualTo("Test Location");
        assertThat(command.role()).isEqualTo("Developer");
        assertThat(command.profileStatus()).isEqualTo(ProfileStatus.ACTIVE);
        assertThat(command.imageUrl()).isEqualTo("http://example.com/image.jpg");
        assertThat(command.skills()).isEqualTo(validSkills);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldThrowException_WhenNameIsInvalid(String name) {
        assertThatThrownBy(() -> new CreateProfileCommand(
                name,
                "Test Bio",
                "Test Location",
                "Developer",
                ProfileStatus.ACTIVE,
                "http://example.com/image.jpg",
                validSkills
        )).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldThrowException_WhenNameIsTooShort() {
        assertThatThrownBy(() -> new CreateProfileCommand(
                "J",
                "Test Bio",
                "Test Location",
                "Developer",
                ProfileStatus.ACTIVE,
                "http://example.com/image.jpg",
                validSkills
        )).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldThrowException_WhenNameIsTooLong() {
        String longName = "a".repeat(101);
        assertThatThrownBy(() -> new CreateProfileCommand(
                longName,
                "Test Bio",
                "Test Location",
                "Developer",
                ProfileStatus.ACTIVE,
                "http://example.com/image.jpg",
                validSkills
        )).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldThrowException_WhenBioIsTooLong() {
        String longBio = "a".repeat(1001);
        assertThatThrownBy(() -> new CreateProfileCommand(
                "John Doe",
                longBio,
                "Test Location",
                "Developer",
                ProfileStatus.ACTIVE,
                "http://example.com/image.jpg",
                validSkills
        )).isInstanceOf(ConstraintViolationException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldThrowException_WhenLocationIsInvalid(String location) {
        assertThatThrownBy(() -> new CreateProfileCommand(
                "John Doe",
                "Test Bio",
                location,
                "Developer",
                ProfileStatus.ACTIVE,
                "http://example.com/image.jpg",
                validSkills
        )).isInstanceOf(ConstraintViolationException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldThrowException_WhenRoleIsInvalid(String role) {
        assertThatThrownBy(() -> new CreateProfileCommand(
                "John Doe",
                "Test Bio",
                "Test Location",
                role,
                ProfileStatus.ACTIVE,
                "http://example.com/image.jpg",
                validSkills
        )).isInstanceOf(ConstraintViolationException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldThrowException_WhenImageUrlIsInvalid(String imageUrl) {
        assertThatThrownBy(() -> new CreateProfileCommand(
                "John Doe",
                "Test Bio",
                "Test Location",
                "Developer",
                ProfileStatus.ACTIVE,
                imageUrl,
                validSkills
        )).isInstanceOf(ConstraintViolationException.class);
    }

//    @Test
//    void shouldThrowException_WhenSkillsIsEmpty() {
//        assertThatThrownBy(() -> new CreateProfileCommand(
//                "John Doe",
//                "Test Bio",
//                "Test Location",
//                "Developer",
//                ProfileStatus.ACTIVE,
//                "http://example.com/image.jpg",
//                Set.of()
//        )).isInstanceOf(ConstraintViolationException.class);
//    }

//    @Test
//    void shouldThrowException_WhenSkillsIsNull() {
//        assertThatThrownBy(() -> new CreateProfileCommand(
//                "John Doe",
//                "Test Bio",
//                "Test Location",
//                "Developer",
//                ProfileStatus.ACTIVE,
//                "http://example.com/image.jpg",
//                null
//        )).isInstanceOf(ConstraintViolationException.class);
//    }

    @Test
    @Disabled
    void shouldThrowException_WhenSkillIsInvalid() {
        Set<Skill> invalidSkills = Set.of(
                Skill.skillWithId(new Skill.SkillId(1L), "Java", "Programming", 11)
        );

        assertThatThrownBy(() -> new CreateProfileCommand(
                "John Doe",
                "Test Bio",
                "Test Location",
                "Developer",
                ProfileStatus.ACTIVE,
                "http://example.com/image.jpg",
                invalidSkills
        ))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("proficiencyLevel: must be");
    }
} 