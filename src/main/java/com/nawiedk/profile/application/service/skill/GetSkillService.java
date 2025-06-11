package com.nawiedk.profile.application.service.skill;

import com.nawiedk.profile.application.port.in.command.skill.GetSkillCommand;
import com.nawiedk.profile.application.port.in.usecase.skill.GetSkillUseCase;
import com.nawiedk.profile.application.port.out.SkillRepositoryPort;
import com.nawiedk.profile.domain.model.Skill;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GetSkillService implements GetSkillUseCase {

    private final SkillRepositoryPort repositoryPort;

    @Override
    public List<Skill> findAll() {
        return repositoryPort.findAll();
    }

    @Override
    public Skill getSKillBySkillId(GetSkillCommand command) {
        return repositoryPort.findSkillBySkillId(command.skillId())
                .orElseThrow(() -> new RuntimeException("Skill not found"));
    }

}
