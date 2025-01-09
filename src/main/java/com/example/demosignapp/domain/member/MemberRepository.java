package com.example.demosignapp.domain.member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findByEmail(String email);

    void save(Member member);
}
