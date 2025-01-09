package com.example.demosignapp.infrastructure.repository;

import com.example.demosignapp.infrastructure.repository.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMemberRepository extends JpaRepository<MemberEntity, Long> {
    MemberEntity findByEmail(String email);
}