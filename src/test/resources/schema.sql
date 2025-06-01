CREATE TABLE IF NOT EXISTS skills (
    skill_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(50),
    proficiency_level INT NOT NULL
);

CREATE TABLE IF NOT EXISTS profiles (
    profile_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    bio VARCHAR(1000),
    location VARCHAR(255) NOT NULL,
    role VARCHAR(100) NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    profile_status VARCHAR(20) NOT NULL,
    user_id BIGINT UNIQUE,
    external_system_id VARCHAR(100) UNIQUE,
    team_id BIGINT,
    manager_id BIGINT
);

CREATE TABLE IF NOT EXISTS profile_skills (
    profile_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    PRIMARY KEY (profile_id, skill_id),
    FOREIGN KEY (profile_id) REFERENCES profiles(profile_id),
    FOREIGN KEY (skill_id) REFERENCES skills(skill_id)
); 