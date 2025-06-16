package com.nawiedk.profile.domain.model;

import com.nawiedk.profile.domain.util.ProjectStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProjectTest {

    @Test
    @Disabled
    void projectWithoutIdShouldSetPreviewTextAndDescription() {
        String name = "Example";
        String preview = "Preview";
        String description = "Description";

        Project project = Project.projectWithoutId(
                name,
                preview,
                description,
                Collections.emptySet(),
                "",
                ProjectStatus.ONGOING
        );

        assertTrue(project.getProjectId().isEmpty());
        assertEquals(preview, project.getPreviewText());
        assertEquals(description, project.getDescription());
    }
}
