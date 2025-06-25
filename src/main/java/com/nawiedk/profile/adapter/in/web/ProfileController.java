package com.nawiedk.profile.adapter.in.web;

import com.nawiedk.profile.adapter.in.web.dto.ProfileCreateDto;
import com.nawiedk.profile.adapter.in.web.dto.ProfileUpdateDto;
import com.nawiedk.profile.adapter.in.web.mapper.request.ProfileRequestMapper;
import com.nawiedk.profile.adapter.in.web.mapper.response.ProfileResponse;
import com.nawiedk.profile.adapter.in.web.mapper.response.ProfileResponseMapper;
import com.nawiedk.profile.application.port.in.command.profile.DeleteProfileCommand;
import com.nawiedk.profile.application.port.in.command.profile.GetProfileCommand;
import com.nawiedk.profile.application.port.in.usecase.profile.CreateProfileUseCase;
import com.nawiedk.profile.application.port.in.usecase.profile.DeleteProfileUseCase;
import com.nawiedk.profile.application.port.in.usecase.profile.GetProfileUseCase;
import com.nawiedk.profile.application.port.in.usecase.profile.UpdateProfileUseCase;
import com.nawiedk.profile.domain.model.Profile;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    private final GetProfileUseCase getProfileUseCase;
    private final CreateProfileUseCase createProfileUseCase;
    private final UpdateProfileUseCase updateProfileUseCase;
    private final DeleteProfileUseCase deleteProfileUseCase;

    private final ProfileResponseMapper profileResponseMapper;
    private final ProfileRequestMapper profileRequestMapper;

    private final List<String> logs = Collections.synchronizedList(new LinkedList<>());

    @GetMapping("/logs/load")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<String>> getLogs() {
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/logs/clear")
    public ResponseEntity<Void> clearLogs() {
        logs.clear();
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping()
    public ResponseEntity<List<ProfileResponse>> getAllProfiles() {

        logger.info("Get all profiles");

        List<ProfileResponse> profiles = getProfileUseCase.getAllProfiles()
                .stream()
                .map(profileResponseMapper::mapToResponse)
                .toList();

        logger.info("Found {} profiles", profiles.size());

        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> getById(@PathVariable Long id, @RequestHeader(value = "X-Client-Domain") String domain) {
        if (!List.of(
                "https://nawied.de",
                "https://www.nawied.de",
                "https://nawied.ch",
                "https://www.nawied.ch",
                "https://nawied.eu",
                "https://www.nawied.eu").contains(domain)
        )
            return ResponseEntity.badRequest().build();

        logger.info("Get profile with profileId: {}", id);

        Profile profile = getProfileUseCase.getProfileByProfileId(
                new GetProfileCommand(id)
        );

        logger.info("Found profile with profileId: {}", id);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'at' HH.mm");
        logs.addFirst(domain + " was visited at: " + LocalDateTime.now().format(formatter));
        if (logs.size() > 1000) {
            logs.removeLast();
        }

        return ResponseEntity.ok(
                profileResponseMapper.mapToResponse(profile)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProfileResponse> create(@Valid @RequestBody ProfileCreateDto dto) {

        logger.info("Create profile with name: {}", dto.getName());

        Profile createdProfile = createProfileUseCase.create(
                profileRequestMapper.generateCreateProfileCommand(dto)
        );

        logger.info("Created profile with profileId: {}", createdProfile.getProfileId().map(Profile.ProfileId::profileId).orElseThrow());

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path(("/{profileId}"))
                        .buildAndExpand(createdProfile.getProfileId().map(Profile.ProfileId::profileId).orElseThrow())
                        .toUri()
        ).body(profileResponseMapper.mapToResponse(createdProfile));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponse> update(@PathVariable Long id, @Valid @RequestBody ProfileUpdateDto dto) {

        logger.info("Update profile with profileId: {}", id);

        dto.setProfileId(id);
        Profile updatedProfile = updateProfileUseCase.update(
                profileRequestMapper.generateUpdateProfileCommand(dto)
        );

        logger.info("Updated profile with profileId: {}", updatedProfile.getProfileId().map(Profile.ProfileId::profileId).orElseThrow());

        return ResponseEntity.ok(
                profileResponseMapper.mapToResponse(updatedProfile)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        logger.info("Delete profile with profileId: {}", id);

        deleteProfileUseCase.deleteByProfileId(new DeleteProfileCommand(id));

        logger.info("Deleted profile with profileId: {}", id);

        return ResponseEntity.noContent().build();
    }
}
