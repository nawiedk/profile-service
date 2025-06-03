package com.nawiedk.profile.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nawiedk.profile.adapter.in.web.dto.ProfileUpdateDto;
import com.nawiedk.profile.adapter.in.web.dto.SkillUpdateDto;
import com.nawiedk.profile.adapter.in.web.mapper.request.ProfileRequestMapper;
import com.nawiedk.profile.adapter.in.web.mapper.response.ProfileResponse;
import com.nawiedk.profile.adapter.in.web.mapper.response.ProfileResponseMapper;
import com.nawiedk.profile.application.port.in.command.CreateProfileCommand;
import com.nawiedk.profile.application.port.in.command.UpdateProfileCommand;
import com.nawiedk.profile.application.port.in.usecase.CreateProfileUseCase;
import com.nawiedk.profile.application.port.in.usecase.DeleteProfileUseCase;
import com.nawiedk.profile.application.port.in.usecase.GetProfileUseCase;
import com.nawiedk.profile.application.port.in.usecase.UpdateProfileUseCase;
import com.nawiedk.profile.domain.model.Profile;
import com.nawiedk.profile.domain.model.Skill;
import com.nawiedk.profile.domain.util.ProfileStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

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

    private ProfileUpdateDto validProfileUpdateDto;
    private Profile domainProfile;
    private ProfileResponse profileResponse;
    private CreateProfileCommand createProfileCommand;
    private UpdateProfileCommand updateProfileCommand;

    @BeforeEach
    void setUp() {
        // Setup valid SkillDto
        SkillUpdateDto skillUpdateDto = new SkillUpdateDto();
        skillUpdateDto.setSkillId(1L);
        skillUpdateDto.setName("Java");
        skillUpdateDto.setCategory("Programming");
        skillUpdateDto.setProficiencyLevel(8);

        // Setup valid ProfileDto
        validProfileUpdateDto = new ProfileUpdateDto();
        validProfileUpdateDto.setProfileId(1L);
        validProfileUpdateDto.setName("John Doe");
        validProfileUpdateDto.setBio("Test Bio");
        validProfileUpdateDto.setLocation("Test Location");
        validProfileUpdateDto.setRole("Developer");
        validProfileUpdateDto.setImageUrl("http://example.com/image.jpg");
        validProfileUpdateDto.setProfileStatus(ProfileStatus.ACTIVE);
        validProfileUpdateDto.setSkills(Set.of(skillUpdateDto));

        // Setup domain Profile
        Skill skill = Skill.skillWithId(new Skill.SkillId(1L), "Java", "Programming", 8);
        domainProfile = Profile.profileWithId(
                new Profile.ProfileId(1L),
                "John Doe",
                "Test Bio",
                "Test Location",
                "Developer",
                "http://example.com/image.jpg",
                ProfileStatus.ACTIVE,
                Set.of(skill)
        );

        // Setup ProfileResponse
        profileResponse = new ProfileResponse(
                1L,
                "John Doe",
                "Test Bio",
                "Test Location",
                "Developer",
                "http://example.com/image.jpg",
                "ACTIVE",
                Set.of(new com.nawiedk.profile.adapter.in.web.mapper.response.SkillResponse(1L, "Java", "Programming", 8))
        );

        // Setup commands
        createProfileCommand = new CreateProfileCommand(
                "John Doe",
                "Test Bio",
                "Test Location",
                "Developer",
                ProfileStatus.ACTIVE,
                "http://example.com/image.jpg",
                Set.of(skill)
        );

        updateProfileCommand = new UpdateProfileCommand(
                1L,
                "John Doe",
                "Test Bio",
                "Test Location",
                "Developer",
                ProfileStatus.ACTIVE,
                "http://example.com/image.jpg",
                Set.of(skill)
        );
    }

    @Test
    void getAllProfiles_shouldReturnListOfProfiles() throws Exception {
        when(getProfileUseCase.findAll()).thenReturn(List.of(domainProfile));
        when(profileResponseMapper.mapToResponse(any(Profile.class))).thenReturn(profileResponse);

        mockMvc.perform(get("/api/profiles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].profileId").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"));

        verify(getProfileUseCase).findAll();
        verify(profileResponseMapper).mapToResponse(any(Profile.class));
    }

    @Test
    void getById_whenProfileExists_shouldReturnProfile() throws Exception {
        when(getProfileUseCase.findProfileById(any())).thenReturn(domainProfile);
        when(profileResponseMapper.mapToResponse(domainProfile)).thenReturn(profileResponse);

        mockMvc.perform(get("/api/profiles/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.profileId").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(getProfileUseCase).findProfileById(any());
        verify(profileResponseMapper).mapToResponse(domainProfile);
    }

    @Test
    void getById_whenProfileNotFound_shouldReturnNotFound() throws Exception {
        when(getProfileUseCase.findProfileById(any())).thenThrow(new RuntimeException("Profile not found"));

        mockMvc.perform(get("/api/profiles/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(getProfileUseCase).findProfileById(any());
    }

    @Test
    void create_whenValidInput_shouldReturnCreatedProfile() throws Exception {
        when(profileRequestMapper.generateCreateProfileCommand(any())).thenReturn(createProfileCommand);
        when(createProfileUseCase.create(any())).thenReturn(domainProfile);
        when(profileResponseMapper.mapToResponse(any())).thenReturn(profileResponse);

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProfileUpdateDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/profiles/1"))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(profileRequestMapper).generateCreateProfileCommand(any());
        verify(createProfileUseCase).create(any());
        verify(profileResponseMapper).mapToResponse(any());
    }

    @Test
    void create_whenInvalidInput_shouldReturnBadRequest() throws Exception {
        validProfileUpdateDto.setName(""); // Invalid name

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProfileUpdateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_whenValidInput_shouldReturnUpdatedProfile() throws Exception {
        when(profileRequestMapper.generateUpdateProfileCommand(any())).thenReturn(updateProfileCommand);
        when(updateProfileUseCase.update(any())).thenReturn(domainProfile);
        when(profileResponseMapper.mapToResponse(any())).thenReturn(profileResponse);

        mockMvc.perform(put("/api/profiles/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProfileUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(profileRequestMapper).generateUpdateProfileCommand(any());
        verify(updateProfileUseCase).update(any());
        verify(profileResponseMapper).mapToResponse(any());
    }

    @Test
    void update_whenProfileNotFound_shouldReturnNotFound() throws Exception {
        when(profileRequestMapper.generateUpdateProfileCommand(any())).thenReturn(updateProfileCommand);
        when(updateProfileUseCase.update(any())).thenThrow(new RuntimeException("Profile not found"));

        mockMvc.perform(put("/api/profiles/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProfileUpdateDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_whenInvalidInput_shouldReturnBadRequest() throws Exception {
        validProfileUpdateDto.setName(""); // Invalid name

        mockMvc.perform(put("/api/profiles/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProfileUpdateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_whenProfileExists_shouldReturnNoContent() throws Exception {
        doNothing().when(deleteProfileUseCase).deleteById(any());

        mockMvc.perform(delete("/api/profiles/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(deleteProfileUseCase).deleteById(any());
    }

    @Test
    void delete_whenProfileNotFound_shouldReturnNotFound() throws Exception {
        doThrow(new RuntimeException("Profile not found"))
                .when(deleteProfileUseCase).deleteById(any());

        mockMvc.perform(delete("/api/profiles/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(deleteProfileUseCase).deleteById(any());
    }
} 