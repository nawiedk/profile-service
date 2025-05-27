package com.nawiedk.profile.application.port.in.usecase;

import com.nawiedk.profile.domain.Profile;

public interface UpdateProfileUseCase {
    Profile update(Long id, Profile updatedProfile);
}
