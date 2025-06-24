package com.nawiedk.profile.adapter.out.persistence.mapper;

import com.nawiedk.profile.adapter.out.persistence.jpaentity.SkillJpaEntity;
import com.nawiedk.profile.domain.model.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper {
    public Skill mapToDomainEntity(SkillJpaEntity entity) {
        return Skill.skillWithId(
                new Skill.SkillId(entity.getSkillId()),
                entity.getName(),
                entity.getCategory()
        );
    }

    public SkillJpaEntity mapToJpaEntity(Skill skill) {
        SkillJpaEntity entity = new SkillJpaEntity();
        entity.setSkillId(skill.getSkillId().map(Skill.SkillId::id).orElse(null));
        entity.setName(skill.getName());
        entity.setCategory(skill.getCategory());
        return entity;
    }
}
