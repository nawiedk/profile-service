package com.nawiedk.profile.adapter.in.web.dto;

import com.nawiedk.profile.domain.util.ProfileStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileUpdateDtoTest {

    private Validator validator;
    private ProfileUpdateDto validDto;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        SkillUpdateDto validSkill = new SkillUpdateDto();
        validSkill.setSkillId(1L);
        validSkill.setName("Java");
        validSkill.setCategory("Programming");
        validSkill.setProficiencyLevel(8);

        validDto = new ProfileUpdateDto();
        validDto.setProfileId(1L);
        validDto.setName("John Doe");
        validDto.setBio("Test Bio");
        validDto.setLocation("Test Location");
        validDto.setRole("Developer");
        validDto.setImageUrl("http://example.com/image.jpg");
        validDto.setProfileStatus(ProfileStatus.ACTIVE);
        validDto.setSkills(Set.of(validSkill));
    }

    @Test
    void shouldValidateValidDto() {
        Set<ConstraintViolation<ProfileUpdateDto>> violations = validator.validate(validDto);
        assertThat(violations).isEmpty();
    }

    @Test
    void shouldValidate_WhenProfileIdIsNull() {
        validDto.setProfileId(null);
        Set<ConstraintViolation<ProfileUpdateDto>> violations = validator.validate(validDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("profileId");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldValidate_WhenNameIsInvalid(String name) {
        validDto.setName(name);
        Set<ConstraintViolation<ProfileUpdateDto>> violations = validator.validate(validDto);
        assertThat(violations).hasSizeGreaterThanOrEqualTo(1);
        assertThat(violations.stream()
                .map(violation -> violation.getPropertyPath().toString()))
                .containsOnly("name");
    }

    @Test
    void shouldValidate_WhenNameIsTooShort() {
        validDto.setName("J");
        Set<ConstraintViolation<ProfileUpdateDto>> violations = validator.validate(validDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("name");
    }

    @Test
    void shouldValidate_WhenNameIsTooLong() {
        validDto.setName("a".repeat(101));
        Set<ConstraintViolation<ProfileUpdateDto>> violations = validator.validate(validDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("name");
    }

    @Test
    void shouldValidate_WhenBioIsTooLong() {
        validDto.setBio("a".repeat(1001));
        Set<ConstraintViolation<ProfileUpdateDto>> violations = validator.validate(validDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("bio");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldValidate_WhenLocationIsInvalid(String location) {
        validDto.setLocation(location);
        Set<ConstraintViolation<ProfileUpdateDto>> violations = validator.validate(validDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("location");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldValidate_WhenRoleIsInvalid(String role) {
        validDto.setRole(role);
        Set<ConstraintViolation<ProfileUpdateDto>> violations = validator.validate(validDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("role");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldValidate_WhenImageUrlIsInvalid(String imageUrl) {
        validDto.setImageUrl(imageUrl);
        Set<ConstraintViolation<ProfileUpdateDto>> violations = validator.validate(validDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("imageUrl");
    }

//    @Test
//    void shouldValidate_WhenSkillsIsEmpty() {
//        validDto.setSkills(Set.of());
//        Set<ConstraintViolation<ProfileUpdateDto>> violations = validator.validate(validDto);
//        assertThat(violations).hasSize(1);
//        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("skills");
//    }

//    @Test
//    void shouldValidate_WhenSkillsIsNull() {
//        validDto.setSkills(null);
//        Set<ConstraintViolation<ProfileUpdateDto>> violations = validator.validate(validDto);
//        assertThat(violations).hasSize(1);
//        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("skills");
//    }

    @Test
    void shouldValidate_WhenSkillIsInvalid() {
        SkillUpdateDto invalidSkill = new SkillUpdateDto();
        invalidSkill.setSkillId(1L);
        invalidSkill.setName("");
        invalidSkill.setCategory("");
        invalidSkill.setProficiencyLevel(11);

        validDto.setSkills(Set.of(invalidSkill));
        Set<ConstraintViolation<ProfileUpdateDto>> violations = validator.validate(validDto);
        assertThat(violations).hasSizeGreaterThan(0);
    }
} 