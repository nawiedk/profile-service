package com.nawiedk.profile.adapter.out.persistence.adapterimpl;

import com.nawiedk.profile.adapter.out.persistence.mapper.SkillMapper;
import com.nawiedk.profile.adapter.out.persistence.repository.SkillRepository;
import com.nawiedk.profile.domain.model.Skill;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SkillPersistenceAdapter {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    public Skill save(Skill skill) {
        return skillMapper.mapToDomainEntity(
                skillRepository.save(skillMapper.mapToJpaEntity(skill))
        );
    }

    public Optional<Skill> findById(Long id) {
        return skillRepository.findById(id)
                .map(skillMapper::mapToDomainEntity);
    }

    public void deleteById(Long id) {
        skillRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return skillRepository.existsById(id);
    }

    public List<Skill> findAll() {
        return skillRepository.findAll()
                .stream()
                .map(skillMapper::mapToDomainEntity)
                .toList();
    }

//    public Optional<Skill> findByName(String name) {
//        return skillRepository.findByName(name)
//                .map(skillMapper::mapToDomainEntity);
//    }
} 