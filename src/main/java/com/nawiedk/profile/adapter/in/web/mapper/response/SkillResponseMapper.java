package com.nawiedk.profile.adapter.in.web.mapper.response;

import com.nawiedk.profile.domain.model.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillResponseMapper {

    public SkillResponse mapToResponse(Skill entity) {
        return new SkillResponse(
                entity.getName(),
                entity.getCategory()
        );
    }
}
