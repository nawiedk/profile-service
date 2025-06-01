package com.nawiedk.profile.adapter.out.persistence.jpaentity;

import com.nawiedk.profile.domain.model.ProfileStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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

    @Column(length = 1000)
    private String bio;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false, length = 100)
    private String role;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProfileStatusEnum profileStatusEnum;

    @Column(unique = true)
    private Long userId;

    @Column(unique = true, length = 100)
    private String externalSystemId;

    @Column
    private Long teamId;

    @Column
    private Long managerId;

    @ManyToMany
    @JoinTable(
            name = "profile_skills",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<SkillJpaEntity> skills;
}