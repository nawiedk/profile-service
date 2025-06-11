package com.nawiedk.profile.application.port.out;

import com.nawiedk.profile.domain.model.Skill;

import java.util.List;
import java.util.Optional;

public interface SkillRepositoryPort {
    Skill save(Skill skill);

    Optional<Skill> findSkillBySkillId(Long skillId);

    void deleteBySkillId(Long skillId);

    boolean existsBySkillId(Long skillId);

    List<Skill> findAll();

    Optional<Skill> findByName(String name);
}
