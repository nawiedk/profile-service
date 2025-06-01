package com.nawiedk.profile.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Optional;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public class Skill {

    @Valid
    private final SkillId skillId;

    @Getter
    @NotBlank
    private final String name;

    @Getter
    @NotBlank
    private final String category;

    @Getter
    @Min(0)
    @Max(10)
    private final int proficiencyLevel;

    public Skill(SkillId skillId, String name, String category, int proficiencyLevel) {
        this.skillId = skillId;
        this.name = name;
        this.category = category;
        this.proficiencyLevel = proficiencyLevel;
        validate(this);
    }

    public record SkillId(@NotNull Long id) {
        public SkillId(Long id) {
            this.id = id;
            validate(this);
        }
    }

    public static Skill skillWithId(SkillId skillId, String name, String category, int proficiencyLevel) {
        return new Skill(skillId, name, category, proficiencyLevel);
    }

    public static Skill skillWithoutId(String name, String category, int proficiencyLevel) {
        return new Skill(null, name, category, proficiencyLevel);
    }

    public Optional<SkillId> getSkillId() {
        return Optional.ofNullable(this.skillId);
    }
}
