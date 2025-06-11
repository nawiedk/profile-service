package com.nawiedk.profile.application.port.in.usecase.skill;

import com.nawiedk.profile.application.port.in.command.skill.DeleteSkillCommand;

public interface DeleteSkillUseCase {
    void deleteBySkillId(DeleteSkillCommand command);
}
