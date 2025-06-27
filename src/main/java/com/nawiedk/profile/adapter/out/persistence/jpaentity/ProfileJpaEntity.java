package com.nawiedk.profile.adapter.out.persistence.jpaentity;

import com.nawiedk.profile.domain.util.ProfileStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "profileId")
public class ProfileJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 2000)
    private String bio;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false, length = 100)
    private String role;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column
    private String githubUrl;

    @Column
    private String linkedInUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProfileStatus profileStatus;

    @ManyToMany
    @JoinTable(
            name = "profile_skills",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<SkillJpaEntity> skills;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "profile_projects",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @OrderBy("name ASC")
    private List<ProjectJpaEntity> projects;

    @Column(nullable = false)
    private Long userId;
}