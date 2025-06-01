package com.nawiedk.profile.domain;

import com.nawiedk.profile.domain.model.Skill;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SkillTest {

    @Test
    void shouldCreateValidSkillWithId() {
        Skill skill = Skill.skillWithId(
                new Skill.SkillId(1L),
                "Java",
                "Programming",
                8
        );

        assertThat(skill.getSkillId()).isPresent();
        assertThat(skill.getSkillId().get().id()).isEqualTo(1L);
        assertThat(skill.getName()).isEqualTo("Java");
        assertThat(skill.getCategory()).isEqualTo("Programming");
        assertThat(skill.getProficiencyLevel()).isEqualTo(8);
    }

    @Test
    void shouldCreateValidSkillWithoutId() {
        Skill skill = Skill.skillWithoutId(
                "Python",
                "Programming",
                5
        );

        assertThat(skill.getSkillId()).isEmpty();
        assertThat(skill.getName()).isEqualTo("Python");
        assertThat(skill.getCategory()).isEqualTo("Programming");
        assertThat(skill.getProficiencyLevel()).isEqualTo(5);
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        assertThatThrownBy(() -> Skill.skillWithoutId(
                "",
                "Programming",
                5
        )).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldThrowExceptionWhenCategoryIsBlank() {
        assertThatThrownBy(() -> Skill.skillWithoutId(
                "Java",
                "",
                5
        )).isInstanceOf(ConstraintViolationException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 11})
    void shouldThrowExceptionWhenProficiencyLevelIsOutOfRange(int level) {
        assertThatThrownBy(() -> Skill.skillWithoutId(
                "Java",
                "Programming",
                level
        ))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("proficiencyLevel: must be");
    }

    @Test
    void shouldThrowExceptionWhenSkillIdIsInvalid() {
        assertThatThrownBy(() -> new Skill.SkillId(null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("id: must not be null");
    }

    @Test
    void shouldAllowZeroProficiencyLevel() {
        Skill skill = Skill.skillWithoutId(
                "Rust",
                "Programming",
                0
        );

        assertThat(skill.getProficiencyLevel()).isZero();
    }

    @Test
    void shouldAllowMaximumProficiencyLevel() {
        Skill skill = Skill.skillWithoutId(
                "Java",
                "Programming",
                10
        );

        assertThat(skill.getProficiencyLevel()).isEqualTo(10);
    }
}
