package com.example.demosignapp.infrastructure.repository.entity;

import com.example.demosignapp.domain.member.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberKey;
    private String name;
    private String email;
    private String profile;

    @Enumerated(EnumType.STRING)
    private Role role;
}
