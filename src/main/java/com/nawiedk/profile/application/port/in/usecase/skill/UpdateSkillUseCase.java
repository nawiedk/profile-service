package com.nawiedk.profile.application.port.in.usecase.skill;

import com.nawiedk.profile.application.port.in.command.skill.UpdateSkillCommand;
import com.nawiedk.profile.domain.model.Skill;

public interface UpdateSkillUseCase {
    Skill update(UpdateSkillCommand command);
}
