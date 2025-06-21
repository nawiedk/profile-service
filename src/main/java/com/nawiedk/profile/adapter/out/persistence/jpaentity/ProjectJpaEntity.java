package com.nawiedk.profile.adapter.out.persistence.jpaentity;

import com.nawiedk.profile.domain.util.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @Column(nullable = false, length = 100, unique = true)
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
    @OrderBy("name ASC")
    private List<SkillJpaEntity> technologies;

    @Column(nullable = false)
    @ManyToMany(mappedBy = "projects")
    private List<ProfileJpaEntity> profiles;

    @Column
    private String githubUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;
}
