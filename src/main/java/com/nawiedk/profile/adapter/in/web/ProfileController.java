package com.nawiedk.profile.adapter.in.web;

import com.nawiedk.profile.adapter.in.web.dto.ProfileCreateDto;
import com.nawiedk.profile.adapter.in.web.dto.ProfileUpdateDto;
import com.nawiedk.profile.adapter.in.web.mapper.request.ProfileRequestMapper;
import com.nawiedk.profile.adapter.in.web.mapper.response.ProfileResponse;
import com.nawiedk.profile.adapter.in.web.mapper.response.ProfileResponseMapper;
import com.nawiedk.profile.application.port.in.command.DeleteProfileCommand;
import com.nawiedk.profile.application.port.in.command.GetProfileCommand;
import com.nawiedk.profile.application.port.in.usecase.CreateProfileUseCase;
import com.nawiedk.profile.application.port.in.usecase.DeleteProfileUseCase;
import com.nawiedk.profile.application.port.in.usecase.GetProfileUseCase;
import com.nawiedk.profile.application.port.in.usecase.UpdateProfileUseCase;
import com.nawiedk.profile.domain.model.Profile;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    private final GetProfileUseCase getProfileUseCase;
    private final CreateProfileUseCase createProfileUseCase;
    private final UpdateProfileUseCase updateProfileUseCase;
    private final DeleteProfileUseCase deleteProfileUseCase;

    private final ProfileResponseMapper profileResponseMapper;
    private final ProfileRequestMapper profileRequestMapper;


    @GetMapping()
    public ResponseEntity<List<ProfileResponse>> getAllProfiles() {

        logger.info("Get all profiles");

        List<ProfileResponse> profiles = getProfileUseCase.findAll()
                .stream()
                .map(profileResponseMapper::mapToResponse)
                .toList();

        logger.info("Found {} profiles", profiles.size());

        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> getById(@PathVariable Long id) {

        logger.info("Get profile with id: {}", id);

        Profile profile = getProfileUseCase.findProfileById(
                new GetProfileCommand(id)
        );

        logger.info("Found profile with id: {}", id);

        return ResponseEntity.ok(
                profileResponseMapper.mapToResponse(profile)
        );
    }

    @PostMapping
    public ResponseEntity<ProfileResponse> create(@Valid @RequestBody ProfileCreateDto dto) {

        logger.info("Create profile with name: {}", dto.getName());

        Profile createdProfile = createProfileUseCase.create(
                profileRequestMapper.generateCreateProfileCommand(dto)
        );

        logger.info("Created profile with id: {}", createdProfile.getProfileId().map(Profile.ProfileId::id).orElseThrow());

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path(("/{id}"))
                        .buildAndExpand(createdProfile.getProfileId().map(Profile.ProfileId::id).orElseThrow())
                        .toUri()
        ).body(profileResponseMapper.mapToResponse(createdProfile));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponse> update(@PathVariable Long id, @Valid @RequestBody ProfileUpdateDto dto) {

        logger.info("Update profile with id: {}", id);

        dto.setProfileId(id);
        Profile updatedProfile = updateProfileUseCase.update(
                profileRequestMapper.generateUpdateProfileCommand(dto)
        );

        logger.info("Updated profile with id: {}", updatedProfile.getProfileId().map(Profile.ProfileId::id).orElseThrow());

        return ResponseEntity.ok(
                profileResponseMapper.mapToResponse(updatedProfile)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        logger.info("Delete profile with id: {}", id);

        deleteProfileUseCase.deleteById(new DeleteProfileCommand(id));

        logger.info("Deleted profile with id: {}", id);

        return ResponseEntity.noContent().build();
    }
}
