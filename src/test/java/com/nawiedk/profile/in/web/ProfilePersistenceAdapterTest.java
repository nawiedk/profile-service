package com.nawiedk.profile.in.web;

import com.nawiedk.profile.adapter.out.persistence.adapterimpl.ProfilePersistenceAdapter;
import com.nawiedk.profile.adapter.out.persistence.jpaentity.ProfileJpaEntity;
import com.nawiedk.profile.adapter.out.persistence.jpaentity.SkillJpaEntity;
import com.nawiedk.profile.adapter.out.persistence.mapper.ProfileMapper;
import com.nawiedk.profile.adapter.out.persistence.mapper.SkillMapper;
import com.nawiedk.profile.adapter.out.persistence.repository.ProfileRepository;
import com.nawiedk.profile.adapter.out.persistence.repository.SkillRepository;
import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.domain.ProfileStatus;
import com.nawiedk.profile.domain.Skill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import({ProfilePersistenceAdapter.class, ProfileMapper.class, SkillMapper.class})
class ProfilePersistenceAdapterTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProfilePersistenceAdapter profilePersistenceAdapter;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private SkillRepository skillRepository;

    private SkillJpaEntity javaSkillEntity;
    private SkillJpaEntity springSkillEntity;

    private Skill javaDomainSkill;
    private Skill springDomainSkill;
    private Skill dockerDomainSkill;

    @BeforeEach
    void setUp() {
        profileRepository.deleteAll();
        skillRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        javaSkillEntity = entityManager.persistAndFlush(new SkillJpaEntity(null, "Java", "Programming Language", 8));
        springSkillEntity = entityManager.persistAndFlush(new SkillJpaEntity(null, "Spring Boot", "Framework", 7));
        SkillJpaEntity dockerSkillEntity = entityManager.persistAndFlush(new SkillJpaEntity(null, "Docker", "DevOps", 6));

        javaDomainSkill = new Skill(new Skill.SkillId(javaSkillEntity.getSkillId()), javaSkillEntity.getName(), javaSkillEntity.getCategory(), javaSkillEntity.getProficiencyLevel());
        springDomainSkill = new Skill(new Skill.SkillId(springSkillEntity.getSkillId()), springSkillEntity.getName(), springSkillEntity.getCategory(), springSkillEntity.getProficiencyLevel());
        dockerDomainSkill = new Skill(new Skill.SkillId(dockerSkillEntity.getSkillId()), dockerSkillEntity.getName(), dockerSkillEntity.getCategory(), dockerSkillEntity.getProficiencyLevel());
    }

    private Profile createTestDomainProfile(String name, String bio, Set<Skill> skills, ProfileStatus status) {
        return Profile.profileWithoutId(
                name,
                bio,
                "Test Location",
                "Test Role",
                status,
                "http://example.com/image.jpg",
                skills
        );
    }

    @Test
    void save_shouldPersistNewProfileAndReturnDomainProfileWithId() {
        Set<Skill> skills = new HashSet<>(Set.of(javaDomainSkill, springDomainSkill));
        Profile profileToSave = createTestDomainProfile("New User", "Bio for new user", skills, ProfileStatus.ACTIVE);

        Profile savedProfile = profilePersistenceAdapter.save(profileToSave);

        assertThat(savedProfile).isNotNull();
        assertThat(savedProfile.getProfileId()).isPresent();
        Long savedProfileId = savedProfile.getProfileId().get().id();

        ProfileJpaEntity foundEntity = entityManager.find(ProfileJpaEntity.class, savedProfileId);
        assertThat(foundEntity).isNotNull();
        assertThat(foundEntity.getName()).isEqualTo("New User");
        assertThat(foundEntity.getBio()).isEqualTo("Bio for new user");
        assertThat(foundEntity.getProfileStatus()).isEqualTo(ProfileStatus.ACTIVE);
        assertThat(foundEntity.getSkills()).hasSize(2);
        Set<String> foundSkillNames = foundEntity.getSkills().stream().map(SkillJpaEntity::getName).collect(Collectors.toSet());
        assertThat(foundSkillNames).containsExactlyInAnyOrder("Java", "Spring Boot");

        assertThat(savedProfile.getName()).isEqualTo("New User");
        assertThat(savedProfile.getSkills()).hasSize(2);
        Set<Long> domainSkillIdsInSavedProfile = savedProfile.getSkills().stream()
                .map(s -> s.getSkillId().get().id())
                .collect(Collectors.toSet());
        assertThat(domainSkillIdsInSavedProfile).containsExactlyInAnyOrder(javaSkillEntity.getSkillId(), springSkillEntity.getSkillId());
    }

    @Test
    void save_shouldUpdateExistingProfile() {
        Set<Skill> initialSkills = new HashSet<>(Set.of(javaDomainSkill));
        Profile initialProfile = createTestDomainProfile("Initial Name", "Initial bio", initialSkills, ProfileStatus.ACTIVE);
        Profile savedInitialProfile = profilePersistenceAdapter.save(initialProfile);
        Long existingProfileId = savedInitialProfile.getProfileId().orElseThrow().id();

        entityManager.flush();
        entityManager.clear();

        Set<Skill> updatedSkills = new HashSet<>(Set.of(springDomainSkill, dockerDomainSkill));
        Profile profileToUpdate = Profile.profileWithId(
                new Profile.ProfileId(existingProfileId),
                "Updated Name",
                "Updated bio",
                "Updated Location",
                "Updated Role",
                "http://example.com/updated.jpg",
                ProfileStatus.INACTIVE,
                updatedSkills
        );

        Profile updatedProfile = profilePersistenceAdapter.save(profileToUpdate);

        assertThat(updatedProfile).isNotNull();
        assertThat(updatedProfile.getProfileId().orElseThrow().id()).isEqualTo(existingProfileId);
        assertThat(updatedProfile.getName()).isEqualTo("Updated Name");
        assertThat(updatedProfile.getBio()).isEqualTo("Updated bio");
        assertThat(updatedProfile.getProfileStatus()).isEqualTo(ProfileStatus.INACTIVE);
        assertThat(updatedProfile.getSkills()).hasSize(2);
        Set<String> updatedDomainSkillNames = updatedProfile.getSkills().stream().map(Skill::getName).collect(Collectors.toSet());
        assertThat(updatedDomainSkillNames).containsExactlyInAnyOrder("Spring Boot", "Docker");

        ProfileJpaEntity foundEntity = entityManager.find(ProfileJpaEntity.class, existingProfileId);
        assertThat(foundEntity).isNotNull();
        assertThat(foundEntity.getName()).isEqualTo("Updated Name");
        assertThat(foundEntity.getProfileStatus()).isEqualTo(ProfileStatus.INACTIVE);
        assertThat(foundEntity.getSkills()).hasSize(2);
        Set<String> foundSkillNames = foundEntity.getSkills().stream().map(SkillJpaEntity::getName).collect(Collectors.toSet());
        assertThat(foundSkillNames).containsExactlyInAnyOrder("Spring Boot", "Docker");
    }

    @Test
    void findById_shouldReturnDomainProfile_whenProfileExists() {
        Set<Skill> skills = new HashSet<>(Set.of(javaDomainSkill));
        Profile profileToSave = createTestDomainProfile("Findable User", "Bio", skills, ProfileStatus.ACTIVE);
        Profile savedProfile = profilePersistenceAdapter.save(profileToSave);
        Long id = savedProfile.getProfileId().orElseThrow().id();

        entityManager.flush();
        entityManager.clear();

        Optional<Profile> foundOptional = profilePersistenceAdapter.findById(id);

        assertThat(foundOptional).isPresent();
        Profile foundProfile = foundOptional.get();
        assertThat(foundProfile.getName()).isEqualTo("Findable User");
        assertThat(foundProfile.getSkills()).hasSize(1);
        assertThat(foundProfile.getSkills().iterator().next().getName()).isEqualTo(javaDomainSkill.getName());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenProfileDoesNotExist() {
        Optional<Profile> foundOptional = profilePersistenceAdapter.findById(999L);
        assertThat(foundOptional).isNotPresent();
    }

    @Test
    void deleteById_shouldRemoveProfile() {
        Set<Skill> skills = new HashSet<>(Set.of(javaDomainSkill));
        Profile profileToSave = createTestDomainProfile("Deletable User", "Bio", skills, ProfileStatus.ACTIVE);
        Profile savedProfile = profilePersistenceAdapter.save(profileToSave);
        Long id = savedProfile.getProfileId().orElseThrow().id();

        assertThat(profileRepository.existsById(id)).isTrue();
        profilePersistenceAdapter.deleteById(id);

        entityManager.flush();
        entityManager.clear();

        assertThat(profileRepository.existsById(id)).isFalse();
    }

    @Test
    void deleteById_shouldNotThrowError_whenProfileDoesNotExist() {
        Long nonExistentId = 999L;
        assertThat(profileRepository.existsById(nonExistentId)).isFalse();

        profilePersistenceAdapter.deleteById(nonExistentId);

        assertThat(profileRepository.existsById(nonExistentId)).isFalse();
    }


    @Test
    void existsById_shouldReturnTrue_whenProfileExists() {
        Set<Skill> skills = new HashSet<>(Set.of(javaDomainSkill));
        Profile profileToSave = createTestDomainProfile("Existing User", "Bio", skills, ProfileStatus.ACTIVE);
        Profile savedProfile = profilePersistenceAdapter.save(profileToSave);
        Long id = savedProfile.getProfileId().orElseThrow().id();

        boolean exists = profilePersistenceAdapter.existsById(id);
        assertThat(exists).isTrue();
    }

    @Test
    void existsById_shouldReturnFalse_whenProfileDoesNotExist() {
        boolean exists = profilePersistenceAdapter.existsById(999L);
        assertThat(exists).isFalse();
    }

    @Test
    void findAll_shouldReturnAllProfiles() {
        Set<Skill> skills1 = new HashSet<>(Set.of(javaDomainSkill));
        Profile profile1 = createTestDomainProfile("User One", "Bio1", skills1, ProfileStatus.ACTIVE);
        profilePersistenceAdapter.save(profile1);

        Set<Skill> skills2 = new HashSet<>(Set.of(springDomainSkill));
        Profile profile2 = createTestDomainProfile("User Two", "Bio2", skills2, ProfileStatus.ON_LEAVE);
        profilePersistenceAdapter.save(profile2);

        entityManager.flush();
        entityManager.clear();

        List<Profile> allProfiles = profilePersistenceAdapter.findAll();

        assertThat(allProfiles).hasSize(2);
        List<String> names = allProfiles.stream().map(Profile::getName).collect(Collectors.toList());
        assertThat(names).containsExactlyInAnyOrder("User One", "User Two");
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoProfilesExist() {
        List<Profile> allProfiles = profilePersistenceAdapter.findAll();
        assertThat(allProfiles).isEmpty();
    }
}
