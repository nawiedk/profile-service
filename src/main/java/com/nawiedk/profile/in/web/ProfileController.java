package com.nawiedk.profile.in.web;

import com.nawiedk.profile.application.usecase.CreateProfileUseCase;
import com.nawiedk.profile.in.web.dto.ProfileDto;
import com.nawiedk.profile.application.usecase.GetProfileUseCase;
import com.nawiedk.profile.domain.Profile;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final GetProfileUseCase getProfileUseCase;
    private final CreateProfileUseCase createProfileUseCase;

    public ProfileController(GetProfileUseCase getProfileUseCase, CreateProfileUseCase createProfileUseCase) {
        this.getProfileUseCase = getProfileUseCase;
        this.createProfileUseCase = createProfileUseCase;
    }

    @GetMapping("/{id}")
    public Profile getById(@PathVariable Long id) {
        return getProfileUseCase.getProfileById(id);
    }

    @PostMapping
    public Profile create(@RequestBody ProfileDto dto) {
        Profile profile = new Profile(
                dto.id,
                dto.name,
                dto.bio,
                dto.location,
                dto.role,
                dto.skills,
                dto.imageUrl,
                dto.profileStatus
        );
        return createProfileUseCase.create(profile);
    }
}
