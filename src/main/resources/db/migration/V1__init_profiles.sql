CREATE TABLE profiles (
                          id BIGINT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          bio TEXT,
                          location VARCHAR(255),
                          role VARCHAR(255),
                          image_url VARCHAR(255),
                          profile_status BOOLEAN
);

CREATE TABLE profile_entity_skills (
                                       profile_entity_id BIGINT,
                                       skills VARCHAR(255)
);

INSERT INTO profiles (id, name, bio, location, role, image_url, profile_status)
VALUES (
           1,
           'Nawied Khaleqi',
           'Ambitious software engineer focused on backend, AI, and DevOps.',
           'Düsseldorf, Germany',
           'Software Engineer',
           'profile.jpg',
           true
       );

INSERT INTO profile_entity_skills (profile_entity_id, skills) VALUES
                                                                  (1, 'Java'),
                                                                  (1, 'Spring'),
                                                                  (1, 'Docker');
