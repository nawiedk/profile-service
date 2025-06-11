package com.nawiedk.profile.application.service.skill;

import com.nawiedk.profile.application.port.in.command.skill.CreateSkillCommand;
import com.nawiedk.profile.application.port.in.usecase.skill.CreateSkillUseCase;
import com.nawiedk.profile.application.port.out.SkillRepositoryPort;
import com.nawiedk.profile.domain.model.Skill;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateSkillService implements CreateSkillUseCase {

    private final SkillRepositoryPort repositoryPort;

    @Override
    public Skill create(CreateSkillCommand command) {
        Skill skill = Skill.skillWithoutId(
                command.name(),
                command.proficiencyLevel()
        );
        return repositoryPort.save(skill);
    }
}
