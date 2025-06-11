package com.nawiedk.profile.application.service.skill;

import com.nawiedk.profile.application.port.in.command.skill.DeleteSkillCommand;
import com.nawiedk.profile.application.port.in.usecase.skill.DeleteSkillUseCase;
import com.nawiedk.profile.application.port.out.SkillRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteSkillService implements DeleteSkillUseCase {

    private final SkillRepositoryPort repositoryPort;

    @Override
    public void deleteBySkillId(DeleteSkillCommand command) {
        if (!repositoryPort.existsBySkillId(command.skillId())) {
            throw new RuntimeException("Skill not found");
        }
        repositoryPort.deleteBySkillId(command.skillId());
    }
}
