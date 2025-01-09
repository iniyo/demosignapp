package com.example.demosignapp.infrastructure.oauth2;

import com.example.demosignapp.domain.member.Member;
import com.example.demosignapp.domain.member.MemberRepository;
import com.example.demosignapp.domain.member.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 소셜에서 넘겨준 attributes
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttribute = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // email, name 등 파싱 - 예시
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        // DB 조회 or 저장
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> Member.builder()
                        .id(null)
                        .memberKey(UUID.randomUUID().toString())
                        .email(email)
                        .name(name)
                        .profile(null)
                        .role(Role.USER)
                        .build());

        memberRepository.save(member);

        return new PrincipalDetails(member, attributes, userNameAttribute);
    }
}
