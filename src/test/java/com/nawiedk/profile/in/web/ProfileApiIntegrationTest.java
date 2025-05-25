package com.nawiedk.profile.in.web;

import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.in.web.dto.ProfileDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProfileApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void postProfile_returnsCreatedProfile() {
        ProfileDto dto = new ProfileDto();
        dto.id = 2L;
        dto.name = "nawiedk";
        dto.bio = "Desc";
        dto.location = "Köln";
        dto.role = "Engineer";
        dto.skills = List.of("React", "Angular");
        dto.imageUrl = "test.jpg";
        dto.profileStatus = true;

        ResponseEntity<Profile> response = rest.postForEntity(url("/api/profiles"), dto, Profile.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("nawiedk", response.getBody().getName());
    }

    @Test
    @SuppressWarnings("unchecked")
    void postProfile_validationFails_returns400() {
        ProfileDto dto = new ProfileDto(); // invalid: all required fields are missing

        ResponseEntity<Map> response = rest.postForEntity(url("/api/profiles"), dto, Map.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, String> errors = (Map<String, String>) response.getBody();

        assertTrue(errors.containsKey("name"));
        assertTrue(errors.containsKey("location"));
        assertTrue(errors.containsKey("role"));
        assertTrue(errors.containsKey("skills"));
        assertTrue(errors.containsKey("imageUrl"));
    }

    @Test
    void putProfile_updatesExistingProfile() {
        ProfileDto dto = new ProfileDto();
        dto.id = 1L;
        dto.name = "Updated Name";
        dto.bio = "Updated Bio";
        dto.location = "Updated City";
        dto.role = "Updated Role";
        dto.skills = List.of("Updated", "Skills");
        dto.imageUrl = "updated.jpg";
        dto.profileStatus = true;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProfileDto> request = new HttpEntity<>(dto, headers);

        ResponseEntity<Profile> response = rest.exchange(
                url("/api/profiles/1"),
                HttpMethod.PUT,
                request,
                Profile.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Name", response.getBody().getName());
    }
}
