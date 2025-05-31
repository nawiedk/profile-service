package com.nawiedk.profile.adapter.in.web.mapper.request;

import com.nawiedk.profile.adapter.in.web.dto.SkillDto;
import com.nawiedk.profile.domain.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillRequestMapper {

    public Skill mapToDomainEntity(SkillDto dto){
        return Skill.skillWithId(
                new Skill.SkillId(dto.getSkillId()),
                dto.getName(),
                dto.getCategory(),
                dto.getProficiencyLevel());
    }
}
