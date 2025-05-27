package com.nawiedk.profile.application.port.in.usecase;

import com.nawiedk.profile.domain.Profile;

public interface CreateProfileUseCase {
    Profile create(Profile profile);
}