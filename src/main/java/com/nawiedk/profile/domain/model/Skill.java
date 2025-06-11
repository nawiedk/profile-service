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
    @Min(0)
    @Max(10)
    private final int proficiencyLevel;

    public Skill(SkillId skillId, String name, int proficiencyLevel) {
        this.skillId = skillId;
        this.name = name;
        this.proficiencyLevel = proficiencyLevel;
        validate(this);
    }

    public record SkillId(@NotNull Long id) {
        public SkillId(@NotNull Long id) {
            if (id == null)
                throw new IllegalArgumentException("Skill ID cannot be null");
            this.id = id;
            validate(this);
        }
    }

    public static Skill skillWithId(SkillId skillId, String name, int proficiencyLevel) {
        return new Skill(skillId, name, proficiencyLevel);
    }

    public static Skill skillWithoutId(String name, int proficiencyLevel) {
        return new Skill(null, name, proficiencyLevel);
    }

    public Optional<SkillId> getSkillId() {
        return Optional.ofNullable(this.skillId);
    }
}
