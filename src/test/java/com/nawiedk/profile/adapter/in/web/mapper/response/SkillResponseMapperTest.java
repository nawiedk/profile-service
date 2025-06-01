package com.nawiedk.profile.adapter.in.web.mapper.response;

import com.nawiedk.profile.domain.model.Skill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SkillResponseMapperTest {

    private SkillResponseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SkillResponseMapper();
    }

    @Test
    void mapToResponse_whenSkillHasId_shouldMapAllFields() {
        Skill skill = Skill.skillWithId(
                new Skill.SkillId(1L),
                "Java",
                "Programming",
                8
        );

        SkillResponse response = mapper.mapToResponse(skill);

        assertThat(response.skillId()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Java");
        assertThat(response.category()).isEqualTo("Programming");
        assertThat(response.proficiencyLevel()).isEqualTo(8);
    }

    @Test
    void mapToResponse_whenSkillHasNoId_shouldMapWithNullId() {
        Skill skill = Skill.skillWithoutId(
                "Python",
                "Programming",
                7
        );

        SkillResponse response = mapper.mapToResponse(skill);

        assertThat(response.skillId()).isNull();
        assertThat(response.name()).isEqualTo("Python");
        assertThat(response.category()).isEqualTo("Programming");
        assertThat(response.proficiencyLevel()).isEqualTo(7);
    }

    @Test
    void mapToResponse_whenSkillHasMaxProficiencyLevel_shouldMapCorrectly() {
        Skill skill = Skill.skillWithId(
                new Skill.SkillId(1L),
                "Spring",
                "Framework",
                10
        );

        SkillResponse response = mapper.mapToResponse(skill);

        assertThat(response.proficiencyLevel()).isEqualTo(10);
    }

    @Test
    void mapToResponse_whenSkillHasMinProficiencyLevel_shouldMapCorrectly() {
        Skill skill = Skill.skillWithId(
                new Skill.SkillId(1L),
                "Docker",
                "DevOps",
                0
        );

        SkillResponse response = mapper.mapToResponse(skill);

        assertThat(response.proficiencyLevel()).isEqualTo(0);
    }
} 