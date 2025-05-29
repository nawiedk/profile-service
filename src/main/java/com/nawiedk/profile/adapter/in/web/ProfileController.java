package com.nawiedk.profile.adapter.in.web;

import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.nawiedk.profile.application.port.in.command.CreateProfileCommand;
import com.nawiedk.profile.application.port.in.command.DeleteProfileCommand;
import com.nawiedk.profile.application.port.in.command.GetProfileCommand;
import com.nawiedk.profile.application.port.in.command.UpdateProfileCommand;
import com.nawiedk.profile.application.port.in.usecase.CreateProfileUseCase;
import com.nawiedk.profile.application.port.in.usecase.DeleteProfileUseCase;
import com.nawiedk.profile.application.port.in.usecase.GetProfileUseCase;
import com.nawiedk.profile.application.port.in.usecase.UpdateProfileUseCase;
import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.adapter.in.ui.dto.ProfileDto;
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


    @GetMapping()
    public ResponseEntity<List<Profile>> getAllProfiles() {
        return ResponseEntity.ok(
                getProfileUseCase.findAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                getProfileUseCase.findProfileById(
                        new GetProfileCommand(id)
                )
        );
    }

    @PostMapping
    public ResponseEntity<Profile> create(@Valid @RequestBody ProfileDto dto) {
        Profile createdProfile = createProfileUseCase.create(
                new CreateProfileCommand(
                        dto.name,
                        dto.bio,
                        dto.location,
                        dto.role,
                        dto.skills,
                        dto.imageUrl,
                        dto.profileStatus
                )
        );

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path(("/{id}"))
                        .buildAndExpand(createdProfile.getId().map(Profile.ProfileId::id).orElseThrow())
                        .toUri()
        ).body(createdProfile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profile> update(@PathVariable Long id, @Valid @RequestBody ProfileDto dto) {
        return ResponseEntity.ok(
                updateProfileUseCase.update(
                        new UpdateProfileCommand(
                                id,
                                dto.name,
                                dto.bio,
                                dto.location,
                                dto.role,
                                dto.skills,
                                dto.imageUrl,
                                dto.profileStatus
                        )
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteProfileUseCase.deleteById(new DeleteProfileCommand(id));
        return ResponseEntity.noContent().build();
    }
}
