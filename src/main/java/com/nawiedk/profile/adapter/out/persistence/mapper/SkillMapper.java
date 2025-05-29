package com.nawiedk.profile.adapter.out.persistence.mapper;

import com.nawiedk.profile.adapter.out.persistence.jpaentity.SkillJpaEntity;
import com.nawiedk.profile.domain.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper {
    public Skill mapToDomainEntity(SkillJpaEntity entity) {
        return Skill.skillWithId(
                new Skill.SkillId(entity.getSkillId()),
                entity.getName(),
                entity.getCategory(),
                entity.getProficiencyLevel()
        );
    }

    public SkillJpaEntity mapToJpaEntity(Skill skill) {
        SkillJpaEntity entity = new SkillJpaEntity();
        entity.setSkillId(skill.getId().map(Skill.SkillId::id).orElse(null));
        entity.setName(skill.getName());
        entity.setCategory(skill.getCategory());
        entity.setProficiencyLevel(skill.getProficiencyLevel());
        return entity;
    }
}
