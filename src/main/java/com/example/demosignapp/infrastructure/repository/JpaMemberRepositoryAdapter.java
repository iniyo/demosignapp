package com.example.demosignapp.infrastructure.repository;

import com.example.demosignapp.domain.member.Member;
import com.example.demosignapp.domain.member.MemberRepository;
import com.example.demosignapp.infrastructure.repository.entity.MemberEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaMemberRepositoryAdapter implements MemberRepository {

    private final JpaMemberRepository jpaMemberRepository;

    public JpaMemberRepositoryAdapter(JpaMemberRepository jpaMemberRepository) {
        this.jpaMemberRepository = jpaMemberRepository;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        // JPA로 조회한 엔티티 -> 도메인 모델 변환
        var entity = jpaMemberRepository.findByEmail(email);
        if (entity == null) {
            return Optional.empty();
        }
        Member member = Member.builder()
                .id(entity.getId())
                .memberKey(entity.getMemberKey())
                .name(entity.getName())
                .email(entity.getEmail())
                .profile(entity.getProfile())
                .role(entity.getRole())
                .build();
        return Optional.of(member);
    }

    @Override
    public void save(Member member) {
        // 도메인 -> JPA 엔티티
        var entity = new MemberEntity(member.getId(),
                member.getMemberKey(),
                member.getName(),
                member.getEmail(),
                member.getProfile(),
                member.getRole());
        jpaMemberRepository.save(entity);
    }
}
