DROP TABLE IF EXISTS profile_skills;
DROP TABLE IF EXISTS skills;
DROP TABLE IF EXISTS profiles;

CREATE TABLE profiles (
                          profile_id BIGINT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          bio TEXT,
                          location VARCHAR(255),
                          role VARCHAR(255),
                          image_url VARCHAR(255),
                          profile_status VARCHAR(20)
);

CREATE TABLE profile_entity_skills (
                                       profile_entity_id BIGINT,
                                       skills VARCHAR(255)
);

CREATE TABLE skills (
                        skill_id BIGINT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL UNIQUE,
                        category VARCHAR(50),
                        proficiency_level INT NOT NULL
);

CREATE TABLE profile_skills (
                                profile_id BIGINT NOT NULL,
                                skill_id BIGINT NOT NULL,
                                PRIMARY KEY (profile_id, skill_id),
                                FOREIGN KEY (profile_id) REFERENCES profiles (profile_id),
                                FOREIGN KEY (skill_id) REFERENCES skills (skill_id)
);

INSERT INTO profiles (profile_id, name, bio, location, role, image_url, profile_status)
VALUES (
           1,
           'Nawied Khaleqi',
           'Ambitious software engineer focused on backend, AI, and DevOps.',
           'Düsseldorf, Germany',
           'Software Engineer',
           'profile.jpg',
           'ACTIVE'
       );

INSERT INTO skills (skill_id, name, category, proficiency_level) VALUES
                                                                     (1, 'Java', 'Programming Language', 5),
                                                                     (2, 'Spring', 'Framework', 4),
                                                                     (3, 'Docker', 'DevOps', 3);

INSERT INTO profile_skills (profile_id, skill_id) VALUES
                                                      (1, 1),
                                                      (1, 2),
                                                      (1, 3);
