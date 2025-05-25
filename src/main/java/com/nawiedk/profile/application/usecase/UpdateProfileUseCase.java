package com.nawiedk.profile.application.usecase;

import com.nawiedk.profile.domain.Profile;

public interface UpdateProfileUseCase {
    Profile update(Long id, Profile updatedProfile);
}
