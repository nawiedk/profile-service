package com.nawiedk.profile.adapter.out.persistence.adapterimpl;

import com.nawiedk.profile.adapter.out.persistence.mapper.SkillMapper;
import com.nawiedk.profile.adapter.out.persistence.repository.SkillRepository;
import com.nawiedk.profile.application.port.out.SkillRepositoryPort;
import com.nawiedk.profile.domain.model.Skill;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SkillPersistenceAdapter implements SkillRepositoryPort {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    public Skill save(Skill skill) {
        return skillMapper.mapToDomainEntity(
                skillRepository.save(skillMapper.mapToJpaEntity(skill))
        );
    }

    public Optional<Skill> findSkillBySkillId(Long skillId) {
        return skillRepository.findById(skillId)
                .map(skillMapper::mapToDomainEntity);
    }

    public void deleteBySkillId(Long skillId) {
        skillRepository.deleteById(skillId);
    }

    public boolean existsBySkillId(Long skillId) {
        return skillRepository.existsById(skillId);
    }

    public List<Skill> findAll() {
        return skillRepository.findAll()
                .stream()
                .map(skillMapper::mapToDomainEntity)
                .toList();
    }

    @Override
    public Optional<Skill> findByName(String name) {
        return skillRepository.findByName(name)
                .map(skillMapper::mapToDomainEntity);
    }
} 