package com.nawiedk.profile.in.web;

import com.nawiedk.profile.adapter.out.persistence.adapterimpl.ProfilePersistenceAdapter;
import com.nawiedk.profile.adapter.out.persistence.jpaentity.ProfileJpaEntity;
import com.nawiedk.profile.adapter.out.persistence.jpaentity.SkillJpaEntity;
import com.nawiedk.profile.adapter.out.persistence.mapper.ProfileMapper;
import com.nawiedk.profile.adapter.out.persistence.mapper.SkillMapper;
import com.nawiedk.profile.adapter.out.persistence.repository.ProfileRepository;
import com.nawiedk.profile.adapter.out.persistence.repository.SkillRepository;
import com.nawiedk.profile.domain.model.Profile;
import com.nawiedk.profile.domain.model.ProfileStatusEnum;
import com.nawiedk.profile.domain.model.Skill;
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

    private Profile createTestDomainProfile(String name, String bio, Set<Skill> skills, ProfileStatusEnum status) {
        return Profile.profileWithoutId(
                name,
                bio,
                "Test Location",
                "Test Role",
                "http://example.com/image.jpg",
                status,
                skills
        );
    }

    @Test
    void save_shouldPersistNewProfileAndReturnDomainProfileWithId() {
        Set<Skill> skills = new HashSet<>(Set.of(javaDomainSkill, springDomainSkill));
        Profile profileToSave = createTestDomainProfile("New User", "Bio for new user", skills, ProfileStatusEnum.ACTIVE);

        Profile savedProfile = profilePersistenceAdapter.save(profileToSave);

        assertThat(savedProfile).isNotNull();
        assertThat(savedProfile.getProfileId()).isPresent();
        Long savedProfileId = savedProfile.getProfileId().get().id();

        ProfileJpaEntity foundEntity = entityManager.find(ProfileJpaEntity.class, savedProfileId);
        assertThat(foundEntity).isNotNull();
        assertThat(foundEntity.getName()).isEqualTo("New User");
        assertThat(foundEntity.getBio()).isEqualTo("Bio for new user");
        assertThat(foundEntity.getProfileStatusEnum()).isEqualTo(ProfileStatusEnum.ACTIVE);
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
        Profile initialProfile = createTestDomainProfile("Initial Name", "Initial bio", initialSkills, ProfileStatusEnum.ACTIVE);
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
                ProfileStatusEnum.INACTIVE,
                updatedSkills
        );

        Profile updatedProfile = profilePersistenceAdapter.save(profileToUpdate);

        assertThat(updatedProfile).isNotNull();
        assertThat(updatedProfile.getProfileId().orElseThrow().id()).isEqualTo(existingProfileId);
        assertThat(updatedProfile.getName()).isEqualTo("Updated Name");
        assertThat(updatedProfile.getBio()).isEqualTo("Updated bio");
        assertThat(updatedProfile.getProfileStatusEnum()).isEqualTo(ProfileStatusEnum.INACTIVE);
        assertThat(updatedProfile.getSkills()).hasSize(2);
        Set<String> updatedDomainSkillNames = updatedProfile.getSkills().stream().map(Skill::getName).collect(Collectors.toSet());
        assertThat(updatedDomainSkillNames).containsExactlyInAnyOrder("Spring Boot", "Docker");

        ProfileJpaEntity foundEntity = entityManager.find(ProfileJpaEntity.class, existingProfileId);
        assertThat(foundEntity).isNotNull();
        assertThat(foundEntity.getName()).isEqualTo("Updated Name");
        assertThat(foundEntity.getProfileStatusEnum()).isEqualTo(ProfileStatusEnum.INACTIVE);
        assertThat(foundEntity.getSkills()).hasSize(2);
        Set<String> foundSkillNames = foundEntity.getSkills().stream().map(SkillJpaEntity::getName).collect(Collectors.toSet());
        assertThat(foundSkillNames).containsExactlyInAnyOrder("Spring Boot", "Docker");
    }

    @Test
    void findById_shouldReturnDomainProfile_whenProfileExists() {
        Set<Skill> skills = new HashSet<>(Set.of(javaDomainSkill));
        Profile profileToSave = createTestDomainProfile("Findable User", "Bio", skills, ProfileStatusEnum.ACTIVE);
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
        Profile profileToSave = createTestDomainProfile("Deletable User", "Bio", skills, ProfileStatusEnum.ACTIVE);
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
        Profile profileToSave = createTestDomainProfile("Existing User", "Bio", skills, ProfileStatusEnum.ACTIVE);
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
        Profile profile1 = createTestDomainProfile("User One", "Bio1", skills1, ProfileStatusEnum.ACTIVE);
        profilePersistenceAdapter.save(profile1);

        Set<Skill> skills2 = new HashSet<>(Set.of(springDomainSkill));
        Profile profile2 = createTestDomainProfile("User Two", "Bio2", skills2, ProfileStatusEnum.ON_LEAVE);
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

    @Test
    void findBySearchCriteria_shouldReturnMatchingProfiles() {
        // Create test profiles with different attributes
        Profile profile1 = createTestDomainProfile("John Developer", "Bio 1", Set.of(javaDomainSkill), ProfileStatusEnum.ACTIVE);
        Profile profile2 = createTestDomainProfile("Jane Engineer", "Bio 2", Set.of(springDomainSkill), ProfileStatusEnum.ON_LEAVE);
        Profile profile3 = createTestDomainProfile("Bob Developer", "Bio 3", Set.of(dockerDomainSkill), ProfileStatusEnum.ACTIVE);

        profile1 = profilePersistenceAdapter.save(profile1);
        profile2 = profilePersistenceAdapter.save(profile2);
        profile3 = profilePersistenceAdapter.save(profile3);

        entityManager.flush();
        entityManager.clear();

        // Test searching by name keyword
        List<Profile> nameResults = profilePersistenceAdapter.findBySearchCriteria(
                "John", null, null, null, null
        );
        assertThat(nameResults).hasSize(1);
        assertThat(nameResults.get(0).getName()).isEqualTo("John Developer");

        // Test searching by role
        List<Profile> roleResults = profilePersistenceAdapter.findBySearchCriteria(
                null, "Test Role", null, null, null
        );
        assertThat(roleResults).hasSize(3);

        // Test searching by status
        List<Profile> statusResults = profilePersistenceAdapter.findBySearchCriteria(
                null, null, null, null, ProfileStatusEnum.ON_LEAVE
        );
        assertThat(statusResults).hasSize(1);
        assertThat(statusResults.get(0).getName()).isEqualTo("Jane Engineer");

        // Test searching by skill keyword
        List<Profile> skillResults = profilePersistenceAdapter.findBySearchCriteria(
                null, null, null, "Java", null
        );
        assertThat(skillResults).hasSize(1);
        assertThat(skillResults.get(0).getName()).isEqualTo("John Developer");

        // Test combined search criteria
        List<Profile> combinedResults = profilePersistenceAdapter.findBySearchCriteria(
                "Developer", "Test Role", null, null, ProfileStatusEnum.ACTIVE
        );
        assertThat(combinedResults).hasSize(2);
        List<String> names = combinedResults.stream().map(Profile::getName).toList();
        assertThat(names).containsExactlyInAnyOrder("John Developer", "Bob Developer");
    }

    @Test
    void findByRole_shouldReturnMatchingProfiles() {
        Profile profile1 = createTestDomainProfile("John", "Bio 1", Set.of(javaDomainSkill), ProfileStatusEnum.ACTIVE);
        profile1 = profilePersistenceAdapter.save(profile1);

        entityManager.flush();
        entityManager.clear();

        List<Profile> results = profilePersistenceAdapter.findByRole("Test Role");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("John");
    }

    @Test
    void findByLocation_shouldReturnMatchingProfiles() {
        Profile profile1 = createTestDomainProfile("John", "Bio 1", Set.of(javaDomainSkill), ProfileStatusEnum.ACTIVE);
        profile1 = profilePersistenceAdapter.save(profile1);

        entityManager.flush();
        entityManager.clear();

        List<Profile> results = profilePersistenceAdapter.findByLocation("Test Location");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("John");
    }

    @Test
    void findBySkillKeyword_shouldReturnMatchingProfiles() {
        Profile profile1 = createTestDomainProfile("John", "Bio 1", Set.of(javaDomainSkill), ProfileStatusEnum.ACTIVE);
        Profile profile2 = createTestDomainProfile("Jane", "Bio 2", Set.of(springDomainSkill), ProfileStatusEnum.ACTIVE);

        profile1 = profilePersistenceAdapter.save(profile1);
        profile2 = profilePersistenceAdapter.save(profile2);

        entityManager.flush();
        entityManager.clear();

        List<Profile> results = profilePersistenceAdapter.findBySkillKeyword("Java");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("John");
    }

    @Test
    void findByProfileStatus_shouldReturnMatchingProfiles() {
        Profile profile1 = createTestDomainProfile("John", "Bio 1", Set.of(javaDomainSkill), ProfileStatusEnum.ACTIVE);
        Profile profile2 = createTestDomainProfile("Jane", "Bio 2", Set.of(springDomainSkill), ProfileStatusEnum.ON_LEAVE);

        profile1 = profilePersistenceAdapter.save(profile1);
        profile2 = profilePersistenceAdapter.save(profile2);

        entityManager.flush();
        entityManager.clear();

        List<Profile> results = profilePersistenceAdapter.findByProfileStatus(ProfileStatusEnum.ON_LEAVE);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Jane");
    }

    @Test
    void updateProfileStatus_shouldUpdateStatusAndReturnUpdatedProfile() {
        Profile profile = createTestDomainProfile("John", "Bio 1", Set.of(javaDomainSkill), ProfileStatusEnum.ACTIVE);
        profile = profilePersistenceAdapter.save(profile);
        Long profileId = profile.getProfileId().orElseThrow().id();

        entityManager.flush();
        entityManager.clear();

        Profile updatedProfile = profilePersistenceAdapter.updateProfileStatus(profileId, ProfileStatusEnum.ON_LEAVE);
        assertThat(updatedProfile.getProfileStatusEnum()).isEqualTo(ProfileStatusEnum.ON_LEAVE);

        Optional<Profile> foundProfile = profilePersistenceAdapter.findById(profileId);
        assertThat(foundProfile).isPresent();
        assertThat(foundProfile.get().getProfileStatusEnum()).isEqualTo(ProfileStatusEnum.ON_LEAVE);
    }

    @Test
    void findByUserId_shouldReturnMatchingProfile() {
        Profile profile = createTestDomainProfile("John", "Bio 1", Set.of(javaDomainSkill), ProfileStatusEnum.ACTIVE);
        profile = profilePersistenceAdapter.save(profile);

        // We need to set the userId directly in the JPA entity since it's not part of the domain model
        ProfileJpaEntity entity = entityManager.find(ProfileJpaEntity.class, profile.getProfileId().orElseThrow().id());
        entity.setUserId(123L);
        entityManager.flush();
        entityManager.clear();

        Optional<Profile> result = profilePersistenceAdapter.findByUserId(123L);
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John");
    }

    @Test
    void findByExternalSystemId_shouldReturnMatchingProfile() {
        Profile profile = createTestDomainProfile("John", "Bio 1", Set.of(javaDomainSkill), ProfileStatusEnum.ACTIVE);
        profile = profilePersistenceAdapter.save(profile);

        // We need to set the externalSystemId directly in the JPA entity since it's not part of the domain model
        ProfileJpaEntity entity = entityManager.find(ProfileJpaEntity.class, profile.getProfileId().orElseThrow().id());
        entity.setExternalSystemId("EXT123");
        entityManager.flush();
        entityManager.clear();

        Optional<Profile> result = profilePersistenceAdapter.findByExternalSystemId("EXT123");
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John");
    }

    @Test
    void findByTeamId_shouldReturnMatchingProfiles() {
        Profile profile1 = createTestDomainProfile("John", "Bio 1", Set.of(javaDomainSkill), ProfileStatusEnum.ACTIVE);
        Profile profile2 = createTestDomainProfile("Jane", "Bio 2", Set.of(springDomainSkill), ProfileStatusEnum.ACTIVE);

        profile1 = profilePersistenceAdapter.save(profile1);
        profile2 = profilePersistenceAdapter.save(profile2);

        // Set teamId for both profiles
        ProfileJpaEntity entity1 = entityManager.find(ProfileJpaEntity.class, profile1.getProfileId().orElseThrow().id());
        entity1.setTeamId(456L);
        ProfileJpaEntity entity2 = entityManager.find(ProfileJpaEntity.class, profile2.getProfileId().orElseThrow().id());
        entity2.setTeamId(456L);

        entityManager.flush();
        entityManager.clear();

        List<Profile> results = profilePersistenceAdapter.findByTeamId(456L);
        assertThat(results).hasSize(2);
        List<String> names = results.stream().map(Profile::getName).toList();
        assertThat(names).containsExactlyInAnyOrder("John", "Jane");
    }

    @Test
    void findByManagerId_shouldReturnMatchingProfiles() {
        Profile profile1 = createTestDomainProfile("John", "Bio 1", Set.of(javaDomainSkill), ProfileStatusEnum.ACTIVE);
        Profile profile2 = createTestDomainProfile("Jane", "Bio 2", Set.of(springDomainSkill), ProfileStatusEnum.ACTIVE);

        profile1 = profilePersistenceAdapter.save(profile1);
        profile2 = profilePersistenceAdapter.save(profile2);

        // Set managerId for both profiles
        ProfileJpaEntity entity1 = entityManager.find(ProfileJpaEntity.class, profile1.getProfileId().orElseThrow().id());
        entity1.setManagerId(789L);
        ProfileJpaEntity entity2 = entityManager.find(ProfileJpaEntity.class, profile2.getProfileId().orElseThrow().id());
        entity2.setManagerId(789L);

        entityManager.flush();
        entityManager.clear();

        List<Profile> results = profilePersistenceAdapter.findByManagerId(789L);
        assertThat(results).hasSize(2);
        List<String> names = results.stream().map(Profile::getName).toList();
        assertThat(names).containsExactlyInAnyOrder("John", "Jane");
    }
}
