package com.nawiedk.profile.application.port.in.usecase;

import com.nawiedk.profile.domain.Profile;

public interface GetProfileUseCase {
    Profile getProfileById(Long id);
}
