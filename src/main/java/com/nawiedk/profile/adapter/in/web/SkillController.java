package com.nawiedk.profile.adapter.in.web;

import com.nawiedk.profile.adapter.in.web.mapper.request.SkillRequestMapper;
import com.nawiedk.profile.adapter.in.web.mapper.response.SkillResponse;
import com.nawiedk.profile.adapter.in.web.mapper.response.SkillResponseMapper;
import com.nawiedk.profile.application.port.in.usecase.skill.CreateSkillUseCase;
import com.nawiedk.profile.application.port.in.usecase.skill.DeleteSkillUseCase;
import com.nawiedk.profile.application.port.in.usecase.skill.GetSkillUseCase;
import com.nawiedk.profile.application.port.in.usecase.skill.UpdateSkillUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
public class SkillController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkillController.class);

    private final GetSkillUseCase getSkillUseCase;
    private final CreateSkillUseCase createSkillUseCase;
    private final UpdateSkillUseCase updateSkillUseCase;
    private final DeleteSkillUseCase deleteSkillUseCase;

    private final SkillResponseMapper skillResponseMapper;
    private final SkillRequestMapper skillRequestMapper;

    public ResponseEntity<List<SkillResponse>> getAllSkills() {
        LOGGER.info("Get all skills");

        List<SkillResponse> skills = getSkillUseCase.findAll()
                .stream()
                .map(skillResponseMapper::mapToResponse)
                .toList();

        LOGGER.info("Found {} skills", skills.size());

        return ResponseEntity.ok(skills);
    }
}
