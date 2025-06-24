package com.nawiedk.profile.application.service.skill;

import com.nawiedk.profile.application.port.in.command.skill.UpdateSkillCommand;
import com.nawiedk.profile.application.port.in.usecase.skill.UpdateSkillUseCase;
import com.nawiedk.profile.application.port.out.SkillRepositoryPort;
import com.nawiedk.profile.domain.model.Skill;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateSkillService implements UpdateSkillUseCase {

    private final SkillRepositoryPort repositoryPort;

    @Override
    public Skill update(UpdateSkillCommand command) {
        repositoryPort.findSkillBySkillId(command.skillId())
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        Skill updatedSkillDomain = Skill.skillWithId(
                new Skill.SkillId(command.skillId()),
                command.name(),
                command.category()
        );
        return repositoryPort.save(updatedSkillDomain);
    }
}
