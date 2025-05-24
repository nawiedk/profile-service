package com.nawiedk.profile.application.usecase;

import com.nawiedk.profile.domain.Profile;

public interface GetProfileUseCase {
    Profile getProfileById(Long id);
}
