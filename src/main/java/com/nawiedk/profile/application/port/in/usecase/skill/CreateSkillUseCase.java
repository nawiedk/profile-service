package com.nawiedk.profile.application.port.in.usecase.skill;

import com.nawiedk.profile.application.port.in.command.skill.CreateSkillCommand;
import com.nawiedk.profile.domain.model.Skill;

public interface CreateSkillUseCase {
    Skill create(CreateSkillCommand command);
}
