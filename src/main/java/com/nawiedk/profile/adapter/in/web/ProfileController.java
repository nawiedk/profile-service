package com.nawiedk.profile.adapter.in.web;

import com.nawiedk.profile.adapter.in.web.dto.ProfileDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final GetProfileUseCase getProfileUseCase;
    private final CreateProfileUseCase createProfileUseCase;
    private final UpdateProfileUseCase updateProfileUseCase;
    private final DeleteProfileUseCase deleteProfileUseCase;

    private final ProfileResponseMapper profileResponseMapper;
    private final ProfileRequestMapper profileRequestMapper;


    @GetMapping()
    public ResponseEntity<List<ProfileResponse>> getAllProfiles() {
        return ResponseEntity.ok(
                getProfileUseCase.findAll()
                        .stream()
                        .map(profileResponseMapper::mapToResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> getById(@PathVariable Long id) {
        Profile profile = getProfileUseCase.findProfileById(
                new GetProfileCommand(id)
        );
        return ResponseEntity.ok(
                profileResponseMapper.mapToResponse(profile)
        );
    }

    @PostMapping
    public ResponseEntity<ProfileResponse> create(@Valid @RequestBody ProfileDto dto) {

        Profile createdProfile = createProfileUseCase.create(
                profileRequestMapper.generateCreateProfileCommand(dto)
        );

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path(("/{id}"))
                        .buildAndExpand(createdProfile.getProfileId().map(Profile.ProfileId::id).orElseThrow())
                        .toUri()
        ).body(profileResponseMapper.mapToResponse(createdProfile));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponse> update(@PathVariable Long id, @Valid @RequestBody ProfileDto dto) {
        dto.setProfileId(id);
        Profile updatedProfile = updateProfileUseCase.update(
                profileRequestMapper.generateUpdateProfileCommand(dto)
        );
        return ResponseEntity.ok(
                profileResponseMapper.mapToResponse(updatedProfile)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteProfileUseCase.deleteById(new DeleteProfileCommand(id));
        return ResponseEntity.noContent().build();
    }
}
