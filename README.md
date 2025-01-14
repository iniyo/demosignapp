# Demosignapp

## 개요
OAuth2 및 JWT 기반 인증을 구현한 인증서버 애플리케이션 - Demo
---

## 기술 스택
- **Spring Boot** 3.x
- **Spring Security** (OAuth2 Client, JWT 인증)
- **Redis** (RefreshToken 저장 및 관리)
- **Lombok** (코드 간결화)
- **Jakarta Validation** (입력 데이터 검증)

---

## 주요 기능
1. **소셜 로그인**:
    - Kakao OAuth2를 통해 로그인 및 AccessToken, RefreshToken 발급.
2. **JWT 인증**:
    - AccessToken을 통한 API 요청 인증.
    - 만료된 AccessToken을 RefreshToken으로 재발급.
3. **로그아웃**:
    - 모든 토큰 무효화.
    - 특정 AccessToken만 무효화.

---

## API 명세

### 1. 소셜 로그인 성공 처리
- **URL**: `/auth/success`
- **Method**: `GET`
- **Query Parameters**:
    - `accessToken` (Optional): 발급된 JWT AccessToken.
- **설명**:
    - 소셜 로그인 성공 후 클라이언트가 `accessToken`을 확인하거나 추가 작업을 수행하는 엔드포인트

---

### 2. AccessToken 재발급
- **URL**: `/auth/reissue`
- **Method**: `POST`
- **Headers**:
    - `Authorization`: Bearer <expiredAccessToken>
- **설명**:
    - 만료된 AccessToken을 RefreshToken을 이용하여 재발급

---

### 3. 모든 토큰 로그아웃
- **URL**: `/auth/logout`
- **Method**: `POST`
- **Headers**:
    - `Authorization`: Bearer <accessToken>
- **설명**:
    - 사용자의 모든 토큰을 무효화

---

### 4. 특정 AccessToken 로그아웃
- **URL**: `/auth/logout/single`
- **Method**: `POST`
- **Headers**:
    - `Authorization`: Bearer <accessToken>
- **설명**:
    - 특정 AccessToken만 무효화

---

### 요청/응답 예시
#### AccessToken 재발급 요청
**Request**:
```http
POST /auth/reissue HTTP/1.1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
