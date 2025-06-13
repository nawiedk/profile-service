package com.nawiedk.profile.adapter.out.persistence.jpaentity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "projectId")
public class ProjectJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String previewText;

    @Column(nullable = false, length = 1000)
    private String description;

    @ManyToMany
    @JoinTable(
            name = "skill_technologies",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<SkillJpaEntity> technologies;

    @Column(nullable = false)
    @ManyToMany(mappedBy = "projects")
    private Set<ProfileJpaEntity> profiles;

    @ElementCollection
    @CollectionTable(
            name = "project_urls",
            joinColumns = @JoinColumn(name = "project_id")
    )
    @Column(name = "url", nullable = false, length = 500)
    private Set<String> urls;
}
