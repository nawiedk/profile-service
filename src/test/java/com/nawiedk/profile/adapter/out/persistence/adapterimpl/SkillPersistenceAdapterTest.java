package com.nawiedk.profile.adapter.out.persistence.adapterimpl;

import com.nawiedk.profile.adapter.out.persistence.jpaentity.SkillJpaEntity;
import com.nawiedk.profile.adapter.out.persistence.mapper.SkillMapper;
import com.nawiedk.profile.adapter.out.persistence.repository.SkillRepository;
import com.nawiedk.profile.domain.model.Skill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({SkillPersistenceAdapter.class, SkillMapper.class})
class SkillPersistenceAdapterTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SkillPersistenceAdapter skillPersistenceAdapter;

    @Autowired
    private SkillRepository skillRepository;

    private SkillJpaEntity javaSkillEntity;
    private Skill javaDomainSkill;

    @BeforeEach
    void setUp() {
        skillRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        javaSkillEntity = entityManager.persistAndFlush(
                new SkillJpaEntity(null, "Java", "Programming Language", 8)
        );

        javaDomainSkill = new Skill(
                new Skill.SkillId(javaSkillEntity.getSkillId()),
                javaSkillEntity.getName(),
                javaSkillEntity.getCategory(),
                javaSkillEntity.getProficiencyLevel()
        );
    }

    @Test
    void save_shouldPersistNewSkillAndReturnDomainSkillWithId() {
        Skill skillToSave = Skill.skillWithoutId("Spring", "Framework", 7);

        Skill savedSkill = skillPersistenceAdapter.save(skillToSave);

        assertThat(savedSkill).isNotNull();
        assertThat(savedSkill.getSkillId()).isPresent();
        Long savedSkillId = savedSkill.getSkillId().get().id();

        SkillJpaEntity foundEntity = entityManager.find(SkillJpaEntity.class, savedSkillId);
        assertThat(foundEntity).isNotNull();
        assertThat(foundEntity.getName()).isEqualTo("Spring");
        assertThat(foundEntity.getCategory()).isEqualTo("Framework");
        assertThat(foundEntity.getProficiencyLevel()).isEqualTo(7);
    }

    @Test
    void save_shouldUpdateExistingSkill() {
        Skill skillToUpdate = Skill.skillWithId(
                new Skill.SkillId(javaSkillEntity.getSkillId()),
                "Java 17",
                "Modern Programming Language",
                9
        );

        Skill updatedSkill = skillPersistenceAdapter.save(skillToUpdate);

        assertThat(updatedSkill).isNotNull();
        assertThat(updatedSkill.getSkillId().get().id()).isEqualTo(javaSkillEntity.getSkillId());
        assertThat(updatedSkill.getName()).isEqualTo("Java 17");
        assertThat(updatedSkill.getCategory()).isEqualTo("Modern Programming Language");
        assertThat(updatedSkill.getProficiencyLevel()).isEqualTo(9);

        SkillJpaEntity foundEntity = entityManager.find(SkillJpaEntity.class, javaSkillEntity.getSkillId());
        assertThat(foundEntity.getName()).isEqualTo("Java 17");
        assertThat(foundEntity.getCategory()).isEqualTo("Modern Programming Language");
        assertThat(foundEntity.getProficiencyLevel()).isEqualTo(9);
    }

    @Test
    void findById_shouldReturnDomainSkill_whenSkillExists() {
        Optional<Skill> foundOptional = skillPersistenceAdapter.findById(javaSkillEntity.getSkillId());

        assertThat(foundOptional).isPresent();
        Skill foundSkill = foundOptional.get();
        assertThat(foundSkill.getName()).isEqualTo("Java");
        assertThat(foundSkill.getCategory()).isEqualTo("Programming Language");
        assertThat(foundSkill.getProficiencyLevel()).isEqualTo(8);
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenSkillDoesNotExist() {
        Optional<Skill> foundOptional = skillPersistenceAdapter.findById(999L);
        assertThat(foundOptional).isEmpty();
    }

    @Test
    void findByName_shouldReturnDomainSkill_whenSkillExists() {
        Optional<Skill> foundOptional = skillPersistenceAdapter.findByName("Java");

        assertThat(foundOptional).isPresent();
        Skill foundSkill = foundOptional.get();
        assertThat(foundSkill.getName()).isEqualTo("Java");
        assertThat(foundSkill.getCategory()).isEqualTo("Programming Language");
        assertThat(foundSkill.getProficiencyLevel()).isEqualTo(8);
    }

    @Test
    void findByName_shouldReturnEmptyOptional_whenSkillDoesNotExist() {
        Optional<Skill> foundOptional = skillPersistenceAdapter.findByName("NonExistent");
        assertThat(foundOptional).isEmpty();
    }

    @Test
    void deleteById_shouldRemoveSkill() {
        assertThat(skillRepository.existsById(javaSkillEntity.getSkillId())).isTrue();

        skillPersistenceAdapter.deleteById(javaSkillEntity.getSkillId());

        entityManager.flush();
        entityManager.clear();

        assertThat(skillRepository.existsById(javaSkillEntity.getSkillId())).isFalse();
    }

    @Test
    void deleteById_shouldNotThrowError_whenSkillDoesNotExist() {
        Long nonExistentId = 999L;
        assertThat(skillRepository.existsById(nonExistentId)).isFalse();

        skillPersistenceAdapter.deleteById(nonExistentId);

        assertThat(skillRepository.existsById(nonExistentId)).isFalse();
    }

    @Test
    void existsById_shouldReturnTrue_whenSkillExists() {
        boolean exists = skillPersistenceAdapter.existsById(javaSkillEntity.getSkillId());
        assertThat(exists).isTrue();
    }

    @Test
    void existsById_shouldReturnFalse_whenSkillDoesNotExist() {
        boolean exists = skillPersistenceAdapter.existsById(999L);
        assertThat(exists).isFalse();
    }

    @Test
    void findAll_shouldReturnAllSkills() {
        SkillJpaEntity springSkillEntity = entityManager.persistAndFlush(
                new SkillJpaEntity(null, "Spring", "Framework", 7)
        );

        List<Skill> allSkills = skillPersistenceAdapter.findAll();

        assertThat(allSkills).hasSize(2);
        List<String> names = allSkills.stream().map(Skill::getName).toList();
        assertThat(names).containsExactlyInAnyOrder("Java", "Spring");
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoSkillsExist() {
        skillRepository.deleteAll();
        entityManager.flush();

        List<Skill> allSkills = skillPersistenceAdapter.findAll();
        assertThat(allSkills).isEmpty();
    }
} 