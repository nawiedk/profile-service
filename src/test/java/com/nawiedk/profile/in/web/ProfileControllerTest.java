package com.nawiedk.profile.in.web;

import com.nawiedk.profile.domain.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProfileControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getById_returnsProfile_whenIdExists() {
        String url = "http://localhost:" + port + "/api/profiles/1";

        Profile response = restTemplate.getForObject(url, Profile.class);

        assertNotNull(response);
        assertEquals("Nawied Khaleqi", response.getName());
        assertEquals("Software Engineer", response.getRole());
        assertTrue(response.getProfileStatus());
    }
}
