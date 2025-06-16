package com.nawiedk.profile.domain.model;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    @Test
    void projectWithoutIdShouldSetPreviewTextAndDescription() {
        String name = "Example";
        String preview = "Preview";
        String description = "Description";

        Project project = Project.projectWithoutId(
                name,
                preview,
                description,
                Collections.emptySet(),
                Collections.emptySet()
        );

        assertTrue(project.getProjectId().isEmpty());
        assertEquals(preview, project.getPreviewText());
        assertEquals(description, project.getDescription());
    }
}
