package com.nawiedk.profile.in.web;

import com.nawiedk.profile.adapter.in.web.dto.ProfileUpdateDto;
import com.nawiedk.profile.adapter.in.web.dto.SkillUpdateDto;
import com.nawiedk.profile.adapter.in.web.mapper.response.ProfileResponse;
import com.nawiedk.profile.adapter.out.persistence.jpaentity.ProfileJpaEntity;
import com.nawiedk.profile.adapter.out.persistence.jpaentity.SkillJpaEntity;
import com.nawiedk.profile.adapter.out.persistence.repository.ProfileRepository;
import com.nawiedk.profile.adapter.out.persistence.repository.SkillRepository;
import com.nawiedk.profile.domain.util.ProfileStatus;
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
        baseUrl = "http://localhost:" + port + "/profiles";
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

    private SkillUpdateDto createSkillDto(SkillJpaEntity skillEntity) {
        SkillUpdateDto dto = new SkillUpdateDto();
        dto.setSkillId(skillEntity.getSkillId());
        dto.setName(skillEntity.getName());
        dto.setCategory(skillEntity.getCategory());
        dto.setProficiencyLevel(skillEntity.getProficiencyLevel());
        return dto;
    }

    private ProfileUpdateDto createValidProfileDto(String name, Set<SkillUpdateDto> skills) {
        ProfileUpdateDto dto = new ProfileUpdateDto();
        dto.setName(name);
        dto.setBio("A bio for " + name);
        dto.setLocation("Location for " + name);
        dto.setRole("Role for " + name);
        dto.setImageUrl("http://example.com/" + name.replace(" ", "").toLowerCase() + ".jpg");
        dto.setProfileStatus(ProfileStatus.ACTIVE);
        dto.setSkills(skills);
        return dto;
    }

    @Test
    void createProfile_whenValidDto_shouldReturnCreatedAndProfile() {
        Set<SkillUpdateDto> skills = new HashSet<>(Set.of(createSkillDto(skillJavaEntity)));
        ProfileUpdateDto profileUpdateDto = createValidProfileDto("Integration User", skills);
        profileUpdateDto.setProfileId(999L);

        ResponseEntity<ProfileResponse> response = restTemplate.postForEntity(baseUrl, profileUpdateDto, ProfileResponse.class);

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

//    @Test
//    void createProfile_whenNameIsBlank_shouldReturnBadRequest() {
//        Set<SkillUpdateDto> skills = new HashSet<>(Set.of(createSkillDto(skillJavaEntity)));
//        ProfileUpdateDto profileUpdateDto = createValidProfileDto("", skills);
//        profileUpdateDto.setProfileId(998L);
//
//        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl, profileUpdateDto, Map.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//        assertThat(response.getBody()).isNotNull();
//        assertThat(response.getBody().get("name")).isEqualTo("must not be blank");
//    }

    @Test
    void getProfileById_whenProfileExists_shouldReturnProfile() {
        Set<SkillUpdateDto> skillsDto = new HashSet<>(Set.of(createSkillDto(skillJavaEntity)));
        ProfileUpdateDto newProfileUpdateDto = createValidProfileDto("Fetch Me", skillsDto);
        newProfileUpdateDto.setProfileId(1000L);
        ResponseEntity<ProfileResponse> postResponse = restTemplate.postForEntity(baseUrl, newProfileUpdateDto, ProfileResponse.class);
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
        Set<SkillUpdateDto> skills1 = new HashSet<>(Set.of(createSkillDto(skillJavaEntity)));
        ProfileUpdateDto profileUpdateDto1 = createValidProfileDto("User Alpha", skills1);
        profileUpdateDto1.setProfileId(1001L);

        Set<SkillUpdateDto> skills2 = new HashSet<>(Set.of(createSkillDto(skillSpringEntity)));
        ProfileUpdateDto profileUpdateDto2 = createValidProfileDto("User Beta", skills2);
        profileUpdateDto2.setProfileId(1002L);

        restTemplate.postForEntity(baseUrl, profileUpdateDto1, ProfileResponse.class);
        restTemplate.postForEntity(baseUrl, profileUpdateDto2, ProfileResponse.class);

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
        Set<SkillUpdateDto> initialSkills = new HashSet<>(Set.of(createSkillDto(skillJavaEntity)));
        ProfileUpdateDto initialProfileUpdateDto = createValidProfileDto("Original Name", initialSkills);
        initialProfileUpdateDto.setProfileId(1003L);
        ResponseEntity<ProfileResponse> postResponse = restTemplate.postForEntity(baseUrl, initialProfileUpdateDto, ProfileResponse.class);
        Long createdId = postResponse.getBody().profileId();

        Set<SkillUpdateDto> updatedSkills = new HashSet<>(Set.of(createSkillDto(skillSpringEntity)));
        ProfileUpdateDto updatedProfileUpdateDto = createValidProfileDto("Updated Name", updatedSkills);
        updatedProfileUpdateDto.setProfileId(createdId);

        HttpEntity<ProfileUpdateDto> requestUpdate = new HttpEntity<>(updatedProfileUpdateDto);
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
        Set<SkillUpdateDto> skills = new HashSet<>(Set.of(createSkillDto(skillJavaEntity)));
        ProfileUpdateDto profileUpdateDto = createValidProfileDto("NonExistent Update", skills);
        profileUpdateDto.setProfileId(99999L);

        HttpEntity<ProfileUpdateDto> requestUpdate = new HttpEntity<>(profileUpdateDto);
        ResponseEntity<Map> response = restTemplate.exchange(baseUrl + "/99999", HttpMethod.PUT, requestUpdate, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().get("error")).isEqualTo("Profile not found");
    }

    @Test
    void deleteProfile_whenProfileExists_shouldReturnNoContent() {
        Set<SkillUpdateDto> skillsDto = new HashSet<>(Set.of(createSkillDto(skillJavaEntity)));
        ProfileUpdateDto newProfileUpdateDto = createValidProfileDto("Delete Me", skillsDto);
        newProfileUpdateDto.setProfileId(1004L);
        ResponseEntity<ProfileResponse> postResponse = restTemplate.postForEntity(baseUrl, newProfileUpdateDto, ProfileResponse.class);
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
