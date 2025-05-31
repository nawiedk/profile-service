package com.nawiedk.profile.adapter.in.web.dto;

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

class SkillDtoTest {

    private Validator validator;
    private SkillDto validDto;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        validDto = new SkillDto();
        validDto.setSkillId(1L);
        validDto.setName("Java");
        validDto.setCategory("Programming");
        validDto.setProficiencyLevel(8);
    }

    @Test
    void shouldValidateValidDto() {
        Set<ConstraintViolation<SkillDto>> violations = validator.validate(validDto);
        assertThat(violations).isEmpty();
    }

    @Test
    void shouldValidate_WhenSkillIdIsNull() {
        validDto.setSkillId(null);
        Set<ConstraintViolation<SkillDto>> violations = validator.validate(validDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("skillId");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldValidate_WhenNameIsInvalid(String name) {
        validDto.setName(name);
        Set<ConstraintViolation<SkillDto>> violations = validator.validate(validDto);
        assertThat(violations).hasSizeGreaterThanOrEqualTo(1);
        assertThat(violations.stream()
                .map(violation -> violation.getPropertyPath().toString()))
                .containsOnly("name");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void shouldValidate_WhenCategoryIsInvalid(String category) {
        validDto.setCategory(category);
        Set<ConstraintViolation<SkillDto>> violations = validator.validate(validDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("category");
    }

    @Test
    void shouldValidate_WhenProficiencyLevelIsTooLow() {
        validDto.setProficiencyLevel(-1);
        Set<ConstraintViolation<SkillDto>> violations = validator.validate(validDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("proficiencyLevel");
    }

    @Test
    void shouldValidate_WhenProficiencyLevelIsTooHigh() {
        validDto.setProficiencyLevel(11);
        Set<ConstraintViolation<SkillDto>> violations = validator.validate(validDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("proficiencyLevel");
    }
} 