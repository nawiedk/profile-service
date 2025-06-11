package com.nawiedk.profile.application.port.in.usecase.skill;

import com.nawiedk.profile.application.port.in.command.skill.GetSkillCommand;
import com.nawiedk.profile.domain.model.Skill;

import java.util.List;

public interface GetSkillUseCase {
    List<Skill> findAll();

    Skill getSKillBySkillId(GetSkillCommand command);
}
