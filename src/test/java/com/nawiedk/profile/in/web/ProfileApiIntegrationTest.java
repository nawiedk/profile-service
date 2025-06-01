package com.nawiedk.profile.in.web;

import com.nawiedk.profile.adapter.in.web.dto.ProfileDto;
import com.nawiedk.profile.adapter.in.web.dto.SkillDto;
import com.nawiedk.profile.adapter.in.web.mapper.response.ProfileResponse;
import com.nawiedk.profile.adapter.out.persistence.jpaentity.ProfileJpaEntity;
import com.nawiedk.profile.adapter.out.persistence.jpaentity.SkillJpaEntity;
import com.nawiedk.profile.adapter.out.persistence.repository.ProfileRepository;
import com.nawiedk.profile.adapter.out.persistence.repository.SkillRepository;
import com.nawiedk.profile.domain.model.ProfileStatusEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProfileApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private SkillRepository skillRepository;

    private String baseUrl;
    private SkillJpaEntity skillJavaEntity;
    private SkillJpaEntity skillSpringEntity;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/profiles";
        skillRepository.deleteAll();
        profileRepository.deleteAll();
        skillJavaEntity = skillRepository.save(new SkillJpaEntity(null, "Java", "Programming", 8));
        skillSpringEntity = skillRepository.save(new SkillJpaEntity(null, "Spring", "Framework", 7));
    }

    @AfterEach
    void tearDown() {
        profileRepository.deleteAll();
        skillRepository.deleteAll();
    }

    private SkillDto createSkillDto(SkillJpaEntity skillEntity) {
        SkillDto dto = new SkillDto();
        dto.setSkillId(skillEntity.getSkillId());
        dto.setName(skillEntity.getName());
        dto.setCategory(skillEntity.getCategory());
        dto.setProficiencyLevel(skillEntity.getProficiencyLevel());
        return dto;
    }

    private ProfileDto createValidProfileDto(String name, Set<SkillDto> skills) {
        ProfileDto dto = new ProfileDto();
        dto.setName(name);
        dto.setBio("A bio for " + name);
        dto.setLocation("Location for " + name);
        dto.setRole("Role for " + name);
        dto.setImageUrl("http://example.com/" + name.replace(" ", "").toLowerCase() + ".jpg");
        dto.setProfileStatusEnum(ProfileStatusEnum.ACTIVE);
        dto.setSkills(skills);
        return dto;
    }

    @Test
    void createProfile_whenValidDto_shouldReturnCreatedAndProfile() {
        Set<SkillDto> skills = new HashSet<>(Set.of(createSkillDto(skillJavaEntity)));
        ProfileDto profileDto = createValidProfileDto("Integration User", skills);
        profileDto.setProfileId(999L);

        ResponseEntity<ProfileResponse> response = restTemplate.postForEntity(baseUrl, profileDto, ProfileResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("Integration User");
        assertThat(response.getBody().skills()).hasSize(1);
        assertThat(response.getBody().skills().iterator().next().name()).isEqualTo("Java");
        assertThat(response.getHeaders().getLocation()).isNotNull();

        Long createdProfileId = response.getBody().profileId();
        Optional<ProfileJpaEntity> foundInDb = profileRepository.findById(createdProfileId);
        assertThat(foundInDb).isPresent();
        assertThat(foundInDb.get().getName()).isEqualTo("Integration User");
    }

    @Test
    void createProfile_whenNameIsBlank_shouldReturnBadRequest() {
        Set<SkillDto> skills = new HashSet<>(Set.of(createSkillDto(skillJavaEntity)));
        ProfileDto profileDto = createValidProfileDto("", skills);
        profileDto.setProfileId(998L);

        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl, profileDto, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("name")).isEqualTo("must not be blank");
    }

    @Test
    void getProfileById_whenProfileExists_shouldReturnProfile() {
        Set<SkillDto> skillsDto = new HashSet<>(Set.of(createSkillDto(skillJavaEntity)));
        ProfileDto newProfileDto = createValidProfileDto("Fetch Me", skillsDto);
        newProfileDto.setProfileId(1000L);
        ResponseEntity<ProfileResponse> postResponse = restTemplate.postForEntity(baseUrl, newProfileDto, ProfileResponse.class);
        Long createdId = postResponse.getBody().profileId();

        ResponseEntity<ProfileResponse> response = restTemplate.getForEntity(baseUrl + "/" + createdId, ProfileResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().profileId()).isEqualTo(createdId);
        assertThat(response.getBody().name()).isEqualTo("Fetch Me");
    }

    @Test
    void getProfileById_whenProfileDoesNotExist_shouldReturnNotFound() {
        ResponseEntity<Map> response = restTemplate.getForEntity(baseUrl + "/99999", Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("Profile not found");
    }

    @Test
    void getAllProfiles_shouldReturnListOfProfiles() {
        Set<SkillDto> skills1 = new HashSet<>(Set.of(createSkillDto(skillJavaEntity)));
        ProfileDto profileDto1 = createValidProfileDto("User Alpha", skills1);
        profileDto1.setProfileId(1001L);

        Set<SkillDto> skills2 = new HashSet<>(Set.of(createSkillDto(skillSpringEntity)));
        ProfileDto profileDto2 = createValidProfileDto("User Beta", skills2);
        profileDto2.setProfileId(1002L);

        restTemplate.postForEntity(baseUrl, profileDto1, ProfileResponse.class);
        restTemplate.postForEntity(baseUrl, profileDto2, ProfileResponse.class);

        ResponseEntity<ProfileResponse[]> response = restTemplate.getForEntity(baseUrl, ProfileResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        List<ProfileResponse> profiles = Arrays.asList(response.getBody());
        assertThat(profiles).hasSize(2);
        List<String> names = profiles.stream().map(ProfileResponse::name).collect(Collectors.toList());
        assertThat(names).containsExactlyInAnyOrder("User Alpha", "User Beta");
    }

    @Test
    void updateProfile_whenProfileExistsAndDtoIsValid_shouldReturnUpdatedProfile() {
        Set<SkillDto> initialSkills = new HashSet<>(Set.of(createSkillDto(skillJavaEntity)));
        ProfileDto initialProfileDto = createValidProfileDto("Original Name", initialSkills);
        initialProfileDto.setProfileId(1003L);
        ResponseEntity<ProfileResponse> postResponse = restTemplate.postForEntity(baseUrl, initialProfileDto, ProfileResponse.class);
        Long createdId = postResponse.getBody().profileId();

        Set<SkillDto> updatedSkills = new HashSet<>(Set.of(createSkillDto(skillSpringEntity)));
        ProfileDto updatedProfileDto = createValidProfileDto("Updated Name", updatedSkills);
        updatedProfileDto.setProfileId(createdId);

        HttpEntity<ProfileDto> requestUpdate = new HttpEntity<>(updatedProfileDto);
        ResponseEntity<ProfileResponse> response = restTemplate.exchange(baseUrl + "/" + createdId, HttpMethod.PUT, requestUpdate, ProfileResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().profileId()).isEqualTo(createdId);
        assertThat(response.getBody().name()).isEqualTo("Updated Name");
        assertThat(response.getBody().skills()).hasSize(1);
        assertThat(response.getBody().skills().iterator().next().name()).isEqualTo("Spring");

        Optional<ProfileJpaEntity> foundInDb = profileRepository.findById(createdId);
        assertThat(foundInDb).isPresent();
        assertThat(foundInDb.get().getName()).isEqualTo("Updated Name");
    }

    @Test
    void updateProfile_whenProfileDoesNotExist_shouldReturnNotFound() {
        Set<SkillDto> skills = new HashSet<>(Set.of(createSkillDto(skillJavaEntity)));
        ProfileDto profileDto = createValidProfileDto("NonExistent Update", skills);
        profileDto.setProfileId(99999L);

        HttpEntity<ProfileDto> requestUpdate = new HttpEntity<>(profileDto);
        ResponseEntity<Map> response = restTemplate.exchange(baseUrl + "/99999", HttpMethod.PUT, requestUpdate, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().get("error")).isEqualTo("Profile not found");
    }

    @Test
    void deleteProfile_whenProfileExists_shouldReturnNoContent() {
        Set<SkillDto> skillsDto = new HashSet<>(Set.of(createSkillDto(skillJavaEntity)));
        ProfileDto newProfileDto = createValidProfileDto("Delete Me", skillsDto);
        newProfileDto.setProfileId(1004L);
        ResponseEntity<ProfileResponse> postResponse = restTemplate.postForEntity(baseUrl, newProfileDto, ProfileResponse.class);
        Long createdId = postResponse.getBody().profileId();

        assertThat(profileRepository.existsById(createdId)).isTrue();

        restTemplate.delete(baseUrl + "/" + createdId);

        ResponseEntity<ProfileResponse> getResponse = restTemplate.getForEntity(baseUrl + "/" + createdId, ProfileResponse.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(profileRepository.existsById(createdId)).isFalse();
    }

    @Test
    void deleteProfile_whenProfileDoesNotExist_shouldReturnNotFound() {
        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + "/88888", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        ResponseEntity<Map> errorResponse = restTemplate.exchange(baseUrl + "/88888", HttpMethod.DELETE, null, Map.class);
        assertThat(errorResponse.getBody().get("error")).isEqualTo("Profile not found");
    }
}
