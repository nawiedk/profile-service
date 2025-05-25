package com.nawiedk.profile.in.web;

import com.nawiedk.profile.application.usecase.GetProfileUseCase;
import com.nawiedk.profile.domain.Profile;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final GetProfileUseCase getProfileUseCase;

    public ProfileController(GetProfileUseCase getProfileUseCase) {
        this.getProfileUseCase = getProfileUseCase;
    }

    @GetMapping("/{id}")
    public Profile getById(@PathVariable Long id) {
        return getProfileUseCase.getProfileById(id);
    }
}
