package com.nawiedk.profile.domain.model;

import jakarta.validation.Valid;
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

    public Skill(SkillId skillId, String name, String category) {
        this.skillId = skillId;
        this.name = name;
        this.category = category;
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

    public static Skill skillWithId(SkillId skillId, String name, String category) {
        return new Skill(skillId, name, category);
    }

    public static Skill skillWithoutId(String name, String category) {
        return new Skill(null, name, category);
    }

    public Optional<SkillId> getSkillId() {
        return Optional.ofNullable(this.skillId);
    }
}
