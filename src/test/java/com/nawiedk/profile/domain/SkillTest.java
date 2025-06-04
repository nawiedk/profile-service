package com.nawiedk.profile.domain;

import com.nawiedk.profile.domain.model.Skill;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SkillTest {

    private static final String VALID_NAME = "Java";
    private static final String VALID_CATEGORY = "Programming Language";
    private static final int VALID_PROFICIENCY_LEVEL = 8;
    private static final Skill.SkillId VALID_SKILL_ID = new Skill.SkillId(1L);

    @Nested
    @DisplayName("Skill Creation Tests")
    class SkillCreationTests {

        @Test
        @DisplayName("Should create valid skill with ID")
        void shouldCreateValidSkillWithId() {
            Skill skill = Skill.skillWithId(
                    VALID_SKILL_ID,
                    VALID_NAME,
                    VALID_CATEGORY,
                    VALID_PROFICIENCY_LEVEL
            );

            assertThat(skill).isNotNull();
            assertThat(skill.getSkillId()).isPresent();
            assertThat(skill.getSkillId().get()).isEqualTo(VALID_SKILL_ID);
            assertThat(skill.getName()).isEqualTo(VALID_NAME);
            assertThat(skill.getCategory()).isEqualTo(VALID_CATEGORY);
            assertThat(skill.getProficiencyLevel()).isEqualTo(VALID_PROFICIENCY_LEVEL);
        }

        @Test
        @DisplayName("Should create valid skill without ID")
        void shouldCreateValidSkillWithoutId() {
            Skill skill = Skill.skillWithoutId(
                    VALID_NAME,
                    VALID_CATEGORY,
                    VALID_PROFICIENCY_LEVEL
            );

            assertThat(skill).isNotNull();
            assertThat(skill.getSkillId()).isEmpty();
            assertThat(skill.getName()).isEqualTo(VALID_NAME);
            assertThat(skill.getCategory()).isEqualTo(VALID_CATEGORY);
            assertThat(skill.getProficiencyLevel()).isEqualTo(VALID_PROFICIENCY_LEVEL);
        }

        @ParameterizedTest
        @DisplayName("Should create skill with all valid proficiency levels")
        @ValueSource(ints = {0, 1, 5, 9, 10})
        void shouldCreateSkillWithValidProficiencyLevels(int proficiencyLevel) {
            Skill skill = Skill.skillWithoutId(
                    VALID_NAME,
                    VALID_CATEGORY,
                    proficiencyLevel
            );

            assertThat(skill).isNotNull();
            assertThat(skill.getProficiencyLevel()).isEqualTo(proficiencyLevel);
        }
    }

    @Nested
    @DisplayName("Skill Validation Tests")
    class SkillValidationTests {

        @Test
        @DisplayName("Should throw exception when name is null")
        void shouldThrowExceptionWhenNameIsNull() {
            assertThatThrownBy(() -> Skill.skillWithoutId(
                    null,
                    VALID_CATEGORY,
                    VALID_PROFICIENCY_LEVEL
            )).isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should throw exception when name is blank")
        void shouldThrowExceptionWhenNameIsBlank() {
            assertThatThrownBy(() -> Skill.skillWithoutId(
                    "   ",
                    VALID_CATEGORY,
                    VALID_PROFICIENCY_LEVEL
            )).isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should throw exception when name is empty")
        void shouldThrowExceptionWhenNameIsEmpty() {
            assertThatThrownBy(() -> Skill.skillWithoutId(
                    "",
                    VALID_CATEGORY,
                    VALID_PROFICIENCY_LEVEL
            )).isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should throw exception when category is null")
        void shouldThrowExceptionWhenCategoryIsNull() {
            assertThatThrownBy(() -> Skill.skillWithoutId(
                    VALID_NAME,
                    null,
                    VALID_PROFICIENCY_LEVEL
            )).isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should throw exception when category is blank")
        void shouldThrowExceptionWhenCategoryIsBlank() {
            assertThatThrownBy(() -> Skill.skillWithoutId(
                    VALID_NAME,
                    "   ",
                    VALID_PROFICIENCY_LEVEL
            )).isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should throw exception when category is empty")
        void shouldThrowExceptionWhenCategoryIsEmpty() {
            assertThatThrownBy(() -> Skill.skillWithoutId(
                    VALID_NAME,
                    "",
                    VALID_PROFICIENCY_LEVEL
            )).isInstanceOf(ConstraintViolationException.class);
        }

        @ParameterizedTest
        @DisplayName("Should throw exception when proficiency level is below minimum")
        @ValueSource(ints = {-1, -5, -10})
        void shouldThrowExceptionWhenProficiencyLevelIsBelowMinimum(int proficiencyLevel) {
            assertThatThrownBy(() -> Skill.skillWithoutId(
                    VALID_NAME,
                    VALID_CATEGORY,
                    proficiencyLevel
            )).isInstanceOf(ConstraintViolationException.class);
        }

        @ParameterizedTest
        @DisplayName("Should throw exception when proficiency level is above maximum")
        @ValueSource(ints = {11, 15, 20})
        void shouldThrowExceptionWhenProficiencyLevelIsAboveMaximum(int proficiencyLevel) {
            assertThatThrownBy(() -> Skill.skillWithoutId(
                    VALID_NAME,
                    VALID_CATEGORY,
                    proficiencyLevel
            )).isInstanceOf(ConstraintViolationException.class);
        }
    }

    @Nested
    @DisplayName("SkillId Tests")
    class SkillIdTests {

        @Test
        @DisplayName("Should create valid SkillId")
        void shouldCreateValidSkillId() {
            Long id = 1L;

            Skill.SkillId skillId = new Skill.SkillId(id);

            assertThat(skillId).isNotNull();
            assertThat(skillId.id()).isEqualTo(id);
        }

        @Test
        @DisplayName("Should throw exception when SkillId is null")
        void shouldThrowExceptionWhenSkillIdIsNull() {
            assertThatThrownBy(() -> new Skill.SkillId(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Skill ID cannot be null");
        }

        @Test
        @DisplayName("Should create SkillId with zero value")
        void shouldCreateSkillIdWithZeroValue() {
            Long id = 0L;

            Skill.SkillId skillId = new Skill.SkillId(id);

            assertThat(skillId).isNotNull();
            assertThat(skillId.id()).isEqualTo(id);
        }

        @Test
        @DisplayName("Should create SkillId with negative value")
        void shouldCreateSkillIdWithNegativeValue() {
            Long id = -1L;

            Skill.SkillId skillId = new Skill.SkillId(id);

            assertThat(skillId).isNotNull();
            assertThat(skillId.id()).isEqualTo(id);
        }
    }

    @Nested
    @DisplayName("Skill Method Tests")
    class SkillMethodTests {

        @Test
        @DisplayName("Should return empty optional when skill has no ID")
        void shouldReturnEmptyOptionalWhenSkillHasNoId() {
            Skill skill = Skill.skillWithoutId(
                    VALID_NAME,
                    VALID_CATEGORY,
                    VALID_PROFICIENCY_LEVEL
            );

            Optional<Skill.SkillId> skillId = skill.getSkillId();

            assertThat(skillId).isEmpty();
        }

        @Test
        @DisplayName("Should return present optional when skill has ID")
        void shouldReturnPresentOptionalWhenSkillHasId() {
            Skill skill = Skill.skillWithId(
                    VALID_SKILL_ID,
                    VALID_NAME,
                    VALID_CATEGORY,
                    VALID_PROFICIENCY_LEVEL
            );

            Optional<Skill.SkillId> skillId = skill.getSkillId();

            assertThat(skillId).isPresent();
            assertThat(skillId.get()).isEqualTo(VALID_SKILL_ID);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should create skill with minimum proficiency level")
        void shouldCreateSkillWithMinimumProficiencyLevel() {
            int minProficiencyLevel = 0;

            Skill skill = Skill.skillWithoutId(
                    VALID_NAME,
                    VALID_CATEGORY,
                    minProficiencyLevel
            );

            assertThat(skill).isNotNull();
            assertThat(skill.getProficiencyLevel()).isEqualTo(minProficiencyLevel);
        }

        @Test
        @DisplayName("Should create skill with maximum proficiency level")
        void shouldCreateSkillWithMaximumProficiencyLevel() {
            int maxProficiencyLevel = 10;

            Skill skill = Skill.skillWithoutId(
                    VALID_NAME,
                    VALID_CATEGORY,
                    maxProficiencyLevel
            );

            assertThat(skill).isNotNull();
            assertThat(skill.getProficiencyLevel()).isEqualTo(maxProficiencyLevel);
        }

        @Test
        @DisplayName("Should create skill with single character name")
        void shouldCreateSkillWithSingleCharacterName() {
            String singleCharName = "R";

            Skill skill = Skill.skillWithoutId(
                    singleCharName,
                    VALID_CATEGORY,
                    VALID_PROFICIENCY_LEVEL
            );

            assertThat(skill).isNotNull();
            assertThat(skill.getName()).isEqualTo(singleCharName);
        }

        @Test
        @DisplayName("Should create skill with long name")
        void shouldCreateSkillWithLongName() {
            String longName = "Very Long Programming Language Name That Could Exist";

            Skill skill = Skill.skillWithoutId(
                    longName,
                    VALID_CATEGORY,
                    VALID_PROFICIENCY_LEVEL
            );

            assertThat(skill).isNotNull();
            assertThat(skill.getName()).isEqualTo(longName);
        }

        @Test
        @DisplayName("Should create skill with single character category")
        void shouldCreateSkillWithSingleCharacterCategory() {
            String singleCharCategory = "L";

            Skill skill = Skill.skillWithoutId(
                    VALID_NAME,
                    singleCharCategory,
                    VALID_PROFICIENCY_LEVEL
            );

            assertThat(skill).isNotNull();
            assertThat(skill.getCategory()).isEqualTo(singleCharCategory);
        }

        @Test
        @DisplayName("Should create skill with long category")
        void shouldCreateSkillWithLongCategory() {
            String longCategory = "Very Specific Category For Programming Languages And Frameworks";

            Skill skill = Skill.skillWithoutId(
                    VALID_NAME,
                    longCategory,
                    VALID_PROFICIENCY_LEVEL
            );

            assertThat(skill).isNotNull();
            assertThat(skill.getCategory()).isEqualTo(longCategory);
        }

        @Test
        @DisplayName("Should create skill with special characters in name")
        void shouldCreateSkillWithSpecialCharactersInName() {
            String nameWithSpecialChars = "C++/C#";

            Skill skill = Skill.skillWithoutId(
                    nameWithSpecialChars,
                    VALID_CATEGORY,
                    VALID_PROFICIENCY_LEVEL
            );

            assertThat(skill).isNotNull();
            assertThat(skill.getName()).isEqualTo(nameWithSpecialChars);
        }

        @Test
        @DisplayName("Should create skill with numbers in category")
        void shouldCreateSkillWithNumbersInCategory() {
            String categoryWithNumbers = "Web 3.0 Technologies";

            Skill skill = Skill.skillWithoutId(
                    VALID_NAME,
                    categoryWithNumbers,
                    VALID_PROFICIENCY_LEVEL
            );

            assertThat(skill).isNotNull();
            assertThat(skill.getCategory()).isEqualTo(categoryWithNumbers);
        }
    }

    @Nested
    @DisplayName("Real World Examples Tests")
    class RealWorldExamplesTests {

        @Test
        @DisplayName("Should create programming language skill")
        void shouldCreateProgrammingLanguageSkill() {
            Skill skill = Skill.skillWithoutId(
                    "Python",
                    "Programming Language",
                    9
            );

            assertThat(skill).isNotNull();
            assertThat(skill.getName()).isEqualTo("Python");
            assertThat(skill.getCategory()).isEqualTo("Programming Language");
            assertThat(skill.getProficiencyLevel()).isEqualTo(9);
        }

        @Test
        @DisplayName("Should create framework skill")
        void shouldCreateFrameworkSkill() {
            Skill skill = Skill.skillWithoutId(
                    "Spring Boot",
                    "Framework",
                    7
            );

            assertThat(skill).isNotNull();
            assertThat(skill.getName()).isEqualTo("Spring Boot");
            assertThat(skill.getCategory()).isEqualTo("Framework");
            assertThat(skill.getProficiencyLevel()).isEqualTo(7);
        }

        @Test
        @DisplayName("Should create database skill")
        void shouldCreateDatabaseSkill() {
            Skill skill = Skill.skillWithoutId(
                    "PostgreSQL",
                    "Database",
                    6
            );

            assertThat(skill).isNotNull();
            assertThat(skill.getName()).isEqualTo("PostgreSQL");
            assertThat(skill.getCategory()).isEqualTo("Database");
            assertThat(skill.getProficiencyLevel()).isEqualTo(6);
        }

        @Test
        @DisplayName("Should create soft skill")
        void shouldCreateSoftSkill() {
            Skill skill = Skill.skillWithoutId(
                    "Leadership",
                    "Soft Skill",
                    8
            );

            assertThat(skill).isNotNull();
            assertThat(skill.getName()).isEqualTo("Leadership");
            assertThat(skill.getCategory()).isEqualTo("Soft Skill");
            assertThat(skill.getProficiencyLevel()).isEqualTo(8);
        }
    }
}