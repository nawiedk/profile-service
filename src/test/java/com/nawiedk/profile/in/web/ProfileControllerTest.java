package com.nawiedk.profile.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nawiedk.profile.adapter.in.web.ProfileController;
import com.nawiedk.profile.adapter.in.web.dto.ProfileDto;
import com.nawiedk.profile.adapter.in.web.dto.SkillDto;
import com.nawiedk.profile.adapter.in.web.mapper.request.ProfileRequestMapper;
import com.nawiedk.profile.adapter.in.web.mapper.response.ProfileResponse;
import com.nawiedk.profile.adapter.in.web.mapper.response.ProfileResponseMapper;
import com.nawiedk.profile.adapter.in.web.mapper.response.SkillResponse;
import com.nawiedk.profile.application.port.in.command.CreateProfileCommand;
import com.nawiedk.profile.application.port.in.command.DeleteProfileCommand;
import com.nawiedk.profile.application.port.in.command.GetProfileCommand;
import com.nawiedk.profile.application.port.in.command.UpdateProfileCommand;
import com.nawiedk.profile.application.port.in.usecase.CreateProfileUseCase;
import com.nawiedk.profile.application.port.in.usecase.DeleteProfileUseCase;
import com.nawiedk.profile.application.port.in.usecase.GetProfileUseCase;
import com.nawiedk.profile.application.port.in.usecase.UpdateProfileUseCase;
import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.domain.ProfileStatus;
import com.nawiedk.profile.domain.Skill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GetProfileUseCase getProfileUseCase;
    @MockBean
    private CreateProfileUseCase createProfileUseCase;
    @MockBean
    private UpdateProfileUseCase updateProfileUseCase;
    @MockBean
    private DeleteProfileUseCase deleteProfileUseCase;

    @MockBean
    private ProfileResponseMapper profileResponseMapper;
    @MockBean
    private ProfileRequestMapper profileRequestMapper;

    private ProfileDto validProfileDto;
    private Profile domainProfile;
    private ProfileResponse profileResponse;
    private CreateProfileCommand createProfileCommand;
    private UpdateProfileCommand updateProfileCommand;
    private Set<SkillDto> skillDtos;
    private Set<Skill> domainSkills;
    private Set<SkillResponse> skillResponses;

    @BeforeEach
    void setUp() {
        SkillDto skillDto1 = new SkillDto();
        skillDto1.setSkillId(1L);
        skillDto1.setName("Java");
        skillDto1.setCategory("Programming Language");
        skillDto1.setProficiencyLevel(8);
        skillDtos = new HashSet<>(Set.of(skillDto1));

        Skill domainSkill1 = Skill.skillWithId(new Skill.SkillId(1L), "Java", "Programming Language", 8);
        domainSkills = new HashSet<>(Set.of(domainSkill1));

        SkillResponse skillResponse1 = new SkillResponse(1L, "Java", "Programming Language", 8);
        skillResponses = new HashSet<>(Set.of(skillResponse1));

        validProfileDto = new ProfileDto();
        validProfileDto.setProfileId(1L);
        validProfileDto.setName("John Doe");
        validProfileDto.setBio("Software Engineer");
        validProfileDto.setLocation("Berlin");
        validProfileDto.setRole("Developer");
        validProfileDto.setImageUrl("http://example.com/image.jpg");
        validProfileDto.setProfileStatus(ProfileStatus.ACTIVE);
        validProfileDto.setSkills(skillDtos);

        domainProfile = Profile.profileWithId(
                new Profile.ProfileId(1L),
                "John Doe",
                "Software Engineer",
                "Berlin",
                "Developer",
                "http://example.com/image.jpg",
                ProfileStatus.ACTIVE,
                domainSkills
        );

        profileResponse = new ProfileResponse(
                1L,
                "John Doe",
                "Software Engineer",
                "Berlin",
                "Developer",
                "http://example.com/image.jpg",
                "ACTIVE",
                skillResponses
        );

        createProfileCommand = new CreateProfileCommand(
                "John Doe",
                "Software Engineer",
                "Berlin",
                "Developer",
                ProfileStatus.ACTIVE,
                "http://example.com/image.jpg",
                domainSkills
        );

        updateProfileCommand = new UpdateProfileCommand(
                1L,
                "John Doe",
                "Software Engineer",
                "Berlin",
                "Developer",
                ProfileStatus.ACTIVE,
                "http://example.com/image.jpg",
                domainSkills
        );
    }

    @Test
    void getAllProfiles_shouldReturnListOfProfileResponses() throws Exception {
        when(getProfileUseCase.findAll()).thenReturn(List.of(domainProfile));
        when(profileResponseMapper.mapToResponse(any(Profile.class))).thenReturn(profileResponse);

        mockMvc.perform(get("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].profileId", is(profileResponse.profileId().intValue())))
                .andExpect(jsonPath("$[0].name", is(profileResponse.name())));
    }

    @Test
    void getAllProfiles_shouldReturnEmptyListWhenNoProfiles() throws Exception {
        when(getProfileUseCase.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getById_whenProfileExists_shouldReturnProfileResponse() throws Exception {
        when(getProfileUseCase.findProfileById(any(GetProfileCommand.class))).thenReturn(domainProfile);
        when(profileResponseMapper.mapToResponse(any(Profile.class))).thenReturn(profileResponse);

        mockMvc.perform(get("/api/profiles/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileId", is(profileResponse.profileId().intValue())))
                .andExpect(jsonPath("$.name", is(profileResponse.name())));
    }

    @Test
    void getById_whenProfileNotFound_shouldReturnNotFound() throws Exception {
        when(getProfileUseCase.findProfileById(any(GetProfileCommand.class))).thenThrow(new RuntimeException("Profile not found"));

        mockMvc.perform(get("/api/profiles/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Profile not found")));
    }

    @Test
    void create_whenValidDto_shouldReturnCreatedProfileResponse() throws Exception {
        when(profileRequestMapper.generateCreateProfileCommand(any(ProfileDto.class))).thenReturn(createProfileCommand);
        when(createProfileUseCase.create(any(CreateProfileCommand.class))).thenReturn(domainProfile);
        when(profileResponseMapper.mapToResponse(any(Profile.class))).thenReturn(profileResponse);

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProfileDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/profiles/1"))
                .andExpect(jsonPath("$.profileId", is(profileResponse.profileId().intValue())))
                .andExpect(jsonPath("$.name", is(profileResponse.name())));
    }

    @Test
    void create_whenInvalidDtoNameBlank_shouldReturnBadRequest() throws Exception {
        ProfileDto invalidDto = new ProfileDto();
        invalidDto.setProfileId(1L);
        invalidDto.setName("");
        invalidDto.setBio("Bio");
        invalidDto.setLocation("Location");
        invalidDto.setRole("Role");
        invalidDto.setImageUrl("img.url");
        invalidDto.setProfileStatus(ProfileStatus.ACTIVE);
        invalidDto.setSkills(skillDtos);


        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name", is("must not be blank")));
    }

    @Test
    void create_whenInvalidDtoNameTooShort_shouldReturnBadRequest() throws Exception {
        ProfileDto invalidDto = new ProfileDto();
        invalidDto.setProfileId(1L);
        invalidDto.setName("A");
        invalidDto.setBio("Bio");
        invalidDto.setLocation("Location");
        invalidDto.setRole("Role");
        invalidDto.setImageUrl("img.url");
        invalidDto.setProfileStatus(ProfileStatus.ACTIVE);
        invalidDto.setSkills(skillDtos);

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name", is("size must be between 2 and 100")));
    }

    @Test
    void create_whenInvalidDtoSkillsEmpty_shouldReturnBadRequest() throws Exception {
        ProfileDto invalidDto = new ProfileDto();
        invalidDto.setProfileId(1L);
        invalidDto.setName("Valid Name");
        invalidDto.setBio("Bio");
        invalidDto.setLocation("Location");
        invalidDto.setRole("Role");
        invalidDto.setImageUrl("img.url");
        invalidDto.setProfileStatus(ProfileStatus.ACTIVE);
        invalidDto.setSkills(Collections.emptySet());

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.skills", is("must not be empty")));
    }

    @Test
    void update_whenValidDtoAndProfileExists_shouldReturnOkProfileResponse() throws Exception {
        when(profileRequestMapper.generateUpdateProfileCommand(any(ProfileDto.class))).thenReturn(updateProfileCommand);
        when(updateProfileUseCase.update(any(UpdateProfileCommand.class))).thenReturn(domainProfile);
        when(profileResponseMapper.mapToResponse(any(Profile.class))).thenReturn(profileResponse);

        mockMvc.perform(put("/api/profiles/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProfileDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileId", is(profileResponse.profileId().intValue())))
                .andExpect(jsonPath("$.name", is(profileResponse.name())));
    }

    @Test
    void update_whenProfileNotFound_shouldReturnNotFound() throws Exception {
        when(profileRequestMapper.generateUpdateProfileCommand(any(ProfileDto.class))).thenReturn(updateProfileCommand);
        when(updateProfileUseCase.update(any(UpdateProfileCommand.class))).thenThrow(new RuntimeException("Profile not found"));

        mockMvc.perform(put("/api/profiles/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProfileDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Profile not found")));
    }

    @Test
    void update_whenInvalidDtoNameBlank_shouldReturnBadRequest() throws Exception {
        ProfileDto invalidDto = new ProfileDto();
        invalidDto.setProfileId(1L);
        invalidDto.setName("");
        invalidDto.setBio("Bio");
        invalidDto.setLocation("Location");
        invalidDto.setRole("Role");
        invalidDto.setImageUrl("img.url");
        invalidDto.setProfileStatus(ProfileStatus.ACTIVE);
        invalidDto.setSkills(skillDtos);

        mockMvc.perform(put("/api/profiles/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name", is("must not be blank")));
    }


    @Test
    void delete_whenProfileExists_shouldReturnNoContent() throws Exception {
        doNothing().when(deleteProfileUseCase).deleteById(any(DeleteProfileCommand.class));

        mockMvc.perform(delete("/api/profiles/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_whenProfileNotFound_shouldReturnNotFound() throws Exception {
        doThrow(new RuntimeException("Profile not found")).when(deleteProfileUseCase).deleteById(any(DeleteProfileCommand.class));

        mockMvc.perform(delete("/api/profiles/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Profile not found")));
    }
}
