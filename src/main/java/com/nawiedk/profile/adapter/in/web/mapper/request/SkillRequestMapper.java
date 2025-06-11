package com.nawiedk.profile.adapter.in.web.mapper.request;

import com.nawiedk.profile.adapter.in.web.dto.SkillUpdateDto;
import com.nawiedk.profile.application.port.in.command.skill.CreateSkillCommand;
import com.nawiedk.profile.application.port.in.command.skill.UpdateSkillCommand;
import com.nawiedk.profile.domain.model.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillRequestMapper {

    public CreateSkillCommand generateCreateSkillCommand(SkillUpdateDto dto) {
        return new CreateSkillCommand(
                dto.getName(),
                dto.getProficiencyLevel()
        );
    }

    public UpdateSkillCommand generateUpdateSkillCommand(SkillUpdateDto dto) {
        return new UpdateSkillCommand(
                dto.getSkillId(),
                dto.getName(),
                dto.getProficiencyLevel()
        );
    }

    public Skill mapToDomainEntity(SkillUpdateDto dto) {
        return Skill.skillWithId(
                new Skill.SkillId(dto.getSkillId()),
                dto.getName(),
                dto.getProficiencyLevel());
    }
}
