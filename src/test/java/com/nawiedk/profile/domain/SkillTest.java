package com.nawiedk.profile.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SkillTest {

    @Test
    void shouldCreateValidSkill() {
        Skill skill = Skill.skillWithoutId("Java", "Programming Language", 8);
        assertEquals("Java", skill.getName());
        assertEquals("Programming Language", skill.getCategory());
        assertEquals(8, skill.getProficiencyLevel());
        assertTrue(skill.getSkillId().isEmpty(), "SkillId should be empty for skillWithoutId");
    }

    @Test
    void shouldCreateValidSkillWithId() {
        Skill.SkillId skillId = new Skill.SkillId(10L);
        Skill skill = Skill.skillWithId(skillId, "Spring Boot", "Framework", 7);

        assertEquals(skillId, skill.getSkillId().orElse(null), "SkillId should be present");
        assertEquals("Spring Boot", skill.getName());
        assertEquals("Framework", skill.getCategory());
        assertEquals(7, skill.getProficiencyLevel());
    }

    @Test
    void shouldFailToCreateSkillWithBlankName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Skill.skillWithoutId(" ", "Programming Language", 8));
        assertTrue(exception.getMessage().toLowerCase().contains("name"));
    }

    @Test
    void shouldFailToCreateSkillWithNullName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Skill.skillWithoutId(null, "Programming Language", 8));
        assertTrue(exception.getMessage().toLowerCase().contains("name"));
    }

    @Test
    void shouldFailToCreateSkillWithBlankCategory() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Skill.skillWithoutId("Java", " ", 8));
        assertTrue(exception.getMessage().toLowerCase().contains("category"));
    }

    @Test
    void shouldFailToCreateSkillWithNullCategory() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Skill.skillWithoutId("Java", null, 8));
        assertTrue(exception.getMessage().toLowerCase().contains("category"));
    }

    @Test
    void shouldFailToCreateSkillWithProficiencyLevelTooLow() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Skill.skillWithoutId("Java", "Programming Language", -1));
        assertTrue(exception.getMessage().toLowerCase().contains("proficiencylevel"));
    }

    @Test
    void shouldFailToCreateSkillWithProficiencyLevelTooHigh() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Skill.skillWithoutId("Java", "Programming Language", 11));
        assertTrue(exception.getMessage().toLowerCase().contains("proficiencylevel"));
    }

    @Test
    void skillIdRecordShouldValidateId() {
        Skill.SkillId validId = new Skill.SkillId(1L);
        assertEquals(1L, validId.id());
        Skill.SkillId nullId = new Skill.SkillId(null);
        assertNull(nullId.id());
    }
}