package com.nawiedk.profile.application.port.in.usecase;

import com.nawiedk.profile.application.port.in.command.GetProfileCommand;
import com.nawiedk.profile.domain.model.Profile;

import java.util.List;

public interface GetProfileUseCase {
    //todo: check if multiple methods are allowed in usecase
    List<Profile> findAll();

    Profile findProfileById(GetProfileCommand command);
}
