package com.nawiedk.profile.adapter.in.web.mapper.request;

import com.nawiedk.profile.adapter.in.web.dto.SkillUpdateDto;
import com.nawiedk.profile.domain.model.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillRequestMapper {

    public Skill mapToDomainEntity(SkillUpdateDto dto) {
        return Skill.skillWithId(
                new Skill.SkillId(dto.getSkillId()),
                dto.getName(),
                dto.getCategory(),
                dto.getProficiencyLevel());
    }
}
