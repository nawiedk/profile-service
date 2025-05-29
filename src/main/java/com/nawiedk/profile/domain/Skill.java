package com.nawiedk.profile.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
@Getter
public class Skill {
    private final SkillId skillId;
    private final String name;
    private final String category;
    private final int proficiencyLevel;

    public record SkillId(Long id){}

    public static Skill skillWithId(SkillId skillId, String name, String category, int proficiencyLevel) {
        return new Skill(skillId, name, category, proficiencyLevel);
    }

    public static Skill skillWithoutId(String name, String category, int proficiencyLevel) {
        return new Skill(null, name, category, proficiencyLevel);
    }

    public Optional<SkillId> getId() {
        return Optional.ofNullable(this.skillId);
    }
}
