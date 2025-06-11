package com.nawiedk.profile.application.port.in.usecase.profile;

import com.nawiedk.profile.application.port.in.command.profile.GetProfileCommand;
import com.nawiedk.profile.domain.model.Profile;

import java.util.List;

public interface GetProfileUseCase {
    Profile findProfileByProfileId(GetProfileCommand command);

    List<Profile> findAll();
}
