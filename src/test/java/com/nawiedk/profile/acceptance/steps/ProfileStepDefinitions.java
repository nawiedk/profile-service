package com.nawiedk.profile.acceptance.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nawiedk.profile.adapter.in.web.dto.ProfileDto;
import com.nawiedk.profile.adapter.in.web.dto.SkillDto;
import com.nawiedk.profile.adapter.in.web.mapper.response.ProfileResponse;
import com.nawiedk.profile.adapter.out.persistence.repository.ProfileRepository;
import com.nawiedk.profile.adapter.out.persistence.repository.SkillRepository;
import com.nawiedk.profile.domain.model.ProfileStatusEnum;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfileStepDefinitions {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @PersistenceContext
    private EntityManager entityManager;

    private ProfileDto currentProfileDto;
    private ResponseEntity<String> lastResponse;
    private Map<String, Object> scenarioContext = new HashMap<>();


    @Before
    @Transactional
    public void setUp() {
        scenarioContext.clear();
        profileRepository.deleteAll();
        skillRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @After
    @Transactional
    public void tearDown() {
        profileRepository.deleteAll();
        skillRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Given("the profile service is running")
    public void theProfileServiceIsRunning() {
        assertThat(port).isNotZero();
    }

    @Given("I have the following skills available in the system:")
    @Transactional
    public void iHaveTheFollowingSkillsAvailable(DataTable dataTable) {
        List<Map<String, String>> skillsData = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> skillRow : skillsData) {
            entityManager.createNativeQuery("INSERT INTO skills (skill_id, name, category, proficiency_level) VALUES (?, ?, ?, ?)")
                    .setParameter(1, Long.parseLong(skillRow.get("skillId")))
                    .setParameter(2, skillRow.get("name"))
                    .setParameter(3, skillRow.get("category"))
                    .setParameter(4, Integer.parseInt(skillRow.get("proficiencyLevel")))
                    .executeUpdate();
        }
        entityManager.flush();
        entityManager.clear();
    }

    @Given("I have a new profile DTO with name {string}, bio {string}, location {string}, role {string} and skill {string} with ID {long} and proficiency {int}")
    public void iHaveANewProfileDto(String name, String bio, String location, String role, String skillName, Long skillId, int proficiency) {
        currentProfileDto = new ProfileDto();
        currentProfileDto.setName(name);
        currentProfileDto.setBio(bio);
        currentProfileDto.setLocation(location);
        currentProfileDto.setRole(role);
        currentProfileDto.setImageUrl("http://example.com/image.jpg");
        currentProfileDto.setProfileStatusEnum(ProfileStatusEnum.ACTIVE);

        SkillDto skillDto = new SkillDto();
        skillDto.setSkillId(skillId);
        skillDto.setName(skillName);
        skillDto.setCategory("From Test Data");
        skillDto.setProficiencyLevel(proficiency);
        currentProfileDto.setSkills(Set.of(skillDto));
    }

    @When("I send a POST request to {string} with the new profile DTO")
    public void iSendAPostRequestToWithTheNewProfileDto(String path) {
        String url = "http://localhost:" + port + path;
        lastResponse = restTemplate.postForEntity(url, currentProfileDto, String.class);
    }

    @Then("the HTTP response status should be {int}")
    public void theHttpResponseStatusShouldBe(int expectedStatus) {
        assertThat(lastResponse.getStatusCode().value()).isEqualTo(expectedStatus);
    }

    @Then("the response should contain the created profile with name {string}")
    public void theResponseShouldContainTheCreatedProfileWithName(String expectedName) throws JsonProcessingException {
        assertThat(lastResponse.getBody()).isNotNull();
        ProfileResponse profileResponse = objectMapper.readValue(lastResponse.getBody(), ProfileResponse.class);
        assertThat(profileResponse.name()).isEqualTo(expectedName);
    }

    @Then("the created profile should have {int} skill named {string} with proficiency {int}")
    public void theCreatedProfileShouldHaveSkillNamedWithProficiency(int skillCount, String skillName, int proficiency) throws JsonProcessingException {
        ProfileResponse profileResponse = objectMapper.readValue(lastResponse.getBody(), ProfileResponse.class);
        assertThat(profileResponse.skills()).hasSize(skillCount);
        Optional<com.nawiedk.profile.adapter.in.web.mapper.response.SkillResponse> foundSkill = profileResponse.skills().stream()
                .filter(s -> s.name().equals(skillName) && s.proficiencyLevel() == proficiency)
                .findFirst();
        assertThat(foundSkill).isPresent();
    }

    @Then("I store the profileId from the response as {string}")
    public void iStoreTheProfileIdFromTheResponseAs(String key) throws JsonProcessingException {
        ProfileResponse profileResponse = objectMapper.readValue(lastResponse.getBody(), ProfileResponse.class);
        assertThat(profileResponse.profileId()).isNotNull();
        scenarioContext.put(key, profileResponse.profileId());
    }

    @Given("I have an updated profile DTO for profileId {string} with name {string}, bio {string}, location {string}, role {string}")
    public void iHaveAnUpdatedProfileDtoForProfileId(String profileIdKey, String name, String bio, String location, String role) {
        Long profileId = (Long) scenarioContext.get(profileIdKey);
        currentProfileDto = new ProfileDto();
        currentProfileDto.setProfileId(profileId);
        currentProfileDto.setName(name);
        currentProfileDto.setBio(bio);
        currentProfileDto.setLocation(location);
        currentProfileDto.setRole(role);
        currentProfileDto.setImageUrl("http://example.com/updated_image.jpg");
        currentProfileDto.setProfileStatusEnum(ProfileStatusEnum.ON_LEAVE);
        currentProfileDto.setSkills(new HashSet<>());
    }

    @Given("the updated DTO includes skill {string} with ID {long} and proficiency {int}")
    public void theUpdatedDtoIncludesSkill(String skillName, Long skillId, int proficiency) {
        SkillDto skillDto = new SkillDto();
        skillDto.setSkillId(skillId);
        skillDto.setName(skillName);
        skillDto.setCategory("From Test Data");
        skillDto.setProficiencyLevel(proficiency);
        currentProfileDto.getSkills().add(skillDto);
    }

    @When("I send a PUT request to {string} with the updated profile DTO")
    public void iSendAPutRequestToWithTheUpdatedProfileDto(String pathTemplate) {
        Long profileId = (Long) scenarioContext.get("currentProfileId");
        String url = "http://localhost:" + port + pathTemplate.replace("{currentProfileId}", profileId.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProfileDto> requestEntity = new HttpEntity<>(currentProfileDto, headers);

        lastResponse = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
    }

    @Then("the response should contain the updated profile with name {string}")
    public void theResponseShouldContainTheUpdatedProfileWithName(String expectedName) throws JsonProcessingException {
        ProfileResponse profileResponse = objectMapper.readValue(lastResponse.getBody(), ProfileResponse.class);
        assertThat(profileResponse.name()).isEqualTo(expectedName);
    }

    @Then("the updated profile should have bio {string}")
    public void theUpdatedProfileShouldHaveBio(String expectedBio) throws JsonProcessingException {
        ProfileResponse profileResponse = objectMapper.readValue(lastResponse.getBody(), ProfileResponse.class);
        assertThat(profileResponse.bio()).isEqualTo(expectedBio);
    }

    @Then("the updated profile should have {int} skills")
    public void theUpdatedProfileShouldHaveSkills(int expectedSkillCount) throws JsonProcessingException {
        ProfileResponse profileResponse = objectMapper.readValue(lastResponse.getBody(), ProfileResponse.class);
        assertThat(profileResponse.skills()).hasSize(expectedSkillCount);
    }

    @When("I send a GET request to {string}")
    public void iSendAGetRequestTo(String pathTemplate) {
        Long profileId = (Long) scenarioContext.get("currentProfileId");
        String url = "http://localhost:" + port + pathTemplate.replace("{currentProfileId}", Objects.toString(profileId, "nonExistentId"));

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            lastResponse = response;
        } catch (Exception e) {
            if (e instanceof org.springframework.web.client.HttpClientErrorException.NotFound) {
                lastResponse = new ResponseEntity<>(((org.springframework.web.client.HttpClientErrorException.NotFound) e).getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            } else {
                throw e;
            }
        }
    }

    @Then("the response should contain the profile with name {string}")
    public void theResponseShouldContainTheProfileWithName(String expectedName) throws JsonProcessingException {
        ProfileResponse profileResponse = objectMapper.readValue(lastResponse.getBody(), ProfileResponse.class);
        assertThat(profileResponse.name()).isEqualTo(expectedName);
    }

    @Then("the profile should have {int} skills including {string} with proficiency {int}")
    public void theProfileShouldHaveSkillsIncluding(int skillCount, String skillName, int proficiency) throws JsonProcessingException {
        ProfileResponse profileResponse = objectMapper.readValue(lastResponse.getBody(), ProfileResponse.class);
        assertThat(profileResponse.skills()).hasSize(skillCount);
        Optional<com.nawiedk.profile.adapter.in.web.mapper.response.SkillResponse> foundSkill = profileResponse.skills().stream()
                .filter(s -> s.name().equals(skillName) && s.proficiencyLevel() == proficiency)
                .findFirst();
        assertThat(foundSkill).isPresent();
    }

    @When("I send a DELETE request to {string}")
    public void iSendADeleteRequestTo(String pathTemplate) {
        Long profileId = (Long) scenarioContext.get("currentProfileId");
        String url = "http://localhost:" + port + pathTemplate.replace("{currentProfileId}", profileId.toString());
        lastResponse = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
    }
}
