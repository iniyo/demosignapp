package com.example.demosignapp.domain.member;

public class Member {

    private Long id; // DB PK (RDB 사용 시)
    private String memberKey;
    private String name;
    private String email;
    private String profile;
    private Role role;

    // 기본 생성자 (protected)
    protected Member() {
        // JPA를 위한 기본 생성자
    }

    // 매개변수를 받는 생성자 (Builder를 통해 호출)
    private Member(Long id, String memberKey, String name, String email, String profile, Role role) {
        this.id = id;
        this.memberKey = memberKey;
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.role = role;
    }

    // Builder 메서드 제공
    public static Builder builder() {
        return new Builder();
    }

    // Builder 클래스
    public static class Builder {
        private Long id;
        private String memberKey;
        private String name;
        private String email;
        private String profile;
        private Role role;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder memberKey(String memberKey) {
            this.memberKey = memberKey;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder profile(String profile) {
            this.profile = profile;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Member build() {
            return new Member(id, memberKey, name, email, profile, role);
        }
    }

    // Getter 메서드
    public Long getId() {
        return id;
    }

    public String getMemberKey() {
        return memberKey;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfile() {
        return profile;
    }

    public Role getRole() {
        return role;
    }

    // Setter 메서드
    public void setId(Long id) {
        this.id = id;
    }

    public void setMemberKey(String memberKey) {
        this.memberKey = memberKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
