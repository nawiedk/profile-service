package com.nawiedk.profile.adapter.out.persistence.jpaentity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    private boolean profileStatus;

    @ElementCollection
    private List<String> skills; //todo: change to relationship with different entity
}