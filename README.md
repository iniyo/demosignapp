# Auth Server (Sign App)

## 개요
OAuth2 및 JWT 기반 인증을 구현한 인증서버 애플리케이션 - Demo,
Redis Cluster 노드 배포 및 관리를 위한 Kubernetes 사용

## 프로젝트 구조
DDD(Domain-Driven Design) 기반으로 다음과 같은 4가지 계층으로 구성:

1. **Interface Layer**:
    - 클라이언트와 직접 통신하는 API 컨트롤러
    - `RestController`와 DTO 객체 정의

2. **Application Layer**:
    - 유스케이스와 비즈니스 로직

3. **Domain Layer**:
    - 핵심 도메인 모델 정의

4. **Infrastructure Layer**:
    - 데이터베이스 및 외부 서비스 연동

---

## 주요 기능

1. **소셜 로그인**:
    - Kakao OAuth2를 통해 로그인 및 AccessToken, RefreshToken 발급. (현재 카카오만 적용)

**1.1 http://localhost:8080/oauth2/authorization/kakao 로 진입시 카카오 인증 페이지로 리다이렉트**

![[Pasted image 20250114163857.png | 500]]

**1.2 성공적으로 로그인 되면 소셜 로그인 성공! 메시지와 함께 Spring Application에서 JWT 발급**

결과:

```
소셜 로그인 성공! 토큰: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhNjQ1MjgzNi1kMzgwLTRhMTQtYTA0My0zZDc3YjMxYjBlMGEiLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzM2ODQwNjM3LCJleHAiOjE3MzY4NDI0Mzd9.PpXB_vK-mhHzl93bOv5CLWmx3lB2rCmjdzf2MwId_2SP3xKghzUK6V6DUFfmbDH6hJTup_MQ7aKM2HAnWK3N9g
```


2. **JWT 인증**:
    - AccessToken을 통한 API 요청 인증.
    - 만료된 AccessToken을 RefreshToken으로 재발급.

3. **로그아웃**:
    - 모든 토큰 무효화.
    - 특정 AccessToken만 무효화.

---  

## 기술 스택

- **Spring Boot**: 3.2.12
- **Spring Security**: OAuth2 Client
- **Redis**: RefreshToken 저장 및 관리
- **Lombok**: 코드 간결화
- **Jakarta Validation**: 입력 데이터 검증
- **JWT**: 인증토큰 발급 및 검증
- **Kubernetes**: Redis 환경 관리 및 앱 배포
- **Docker**: Docker Image 생성 및 Desktop에 배포

---

## 주요 API  명세 및 요청/응답 예시

### 1. 소셜 로그인

- **URL**: `/auth/success`
- **AuthURL** `/oauth2/authorization/kakao`
- **Method**: `GET`
- **Query Parameters**:
    - `accessToken` (Optional): 발급된 JWT AccessToken.
- **설명**:  
  소셜 로그인 성공 후 클라이언트가 `accessToken`을 확인하거나 추가 작업을 수행하는 엔드포인트.

**Request**:

```http
`GET /auth/success?accessToken=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`
```

**Response**:

```json
{
  "message": "소셜 로그인 성공! 토큰: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```


---  

### 2. AccessToken 재발급

- **URL**: `/auth/reissue`
- **Method**: `POST`
- **Headers**:
    - `Authorization`: Bearer `<expiredAccessToken>
-  **설명**:
    - 만료된 AccessToken을 RefreshToken을 이용하여 재발급

**Request**:
```http
POST /auth/reissue HTTP/1.1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response**:
```json
{
  "newAccessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "AccessToken이 성공적으로 재발급되었습니다."
}
```

---


### 3. 모든 토큰 로그아웃

- **URL**: `/auth/logout`
- **Method**: `POST`
- **Headers**:
    - `Authorization`: Bearer `<accessToken>
- **설명**:
    - 사용자의 모든 토큰을 무효화


**Request**:
```http
POST /auth/reissue HTTP/1.1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response**:
```json
{
  "message": "모든 토큰이 무효화되었습니다.",
  "invalidatedToken": null
}
```

---
### 4. 특정 AccessToken 로그아웃
- **URL**: `/auth/logout/single`
- **Method**: `POST`
- **Headers**:
    - `Authorization`: Bearer `<accessToken>
- **설명**:
    - 특정 AccessToken만 무효화

**Request**:
```http
POST /auth/reissue HTTP/1.1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response**:
```json
{
  "message": "AccessToken이 무효화되었습니다.",
  "invalidatedToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```


---

### Social Login, Logout Flow - Plant UML

![[Pasted image 20250113163322.png]]


## 향후 보완 사항

에러 처리 및 문서화 진행(Swagger), Controller 역할 구분 및 소셜 로그인의 경우 소셜 로그인을 진행할 수 있는 API Endpoint가 필요,

## 참고사이트

- 스프링 시큐리티, OAuth, 인증 프로세스  
  [spring security OAuth2 동작 원리 (Velog)](https://velog.io/@nefertiri/%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0-OAuth2-%EB%8F%99%EC%9E%91-%EC%9B%90%EB%A6%AC#oauth2userrequestentityconverter)  
  [스프링 시큐리티 - 인증 프로세스(Velog)](https://velog.io/@impala/Spring-Security-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0-%EC%9D%B8%EC%A6%9D-%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4)  
  [Spring security JWT 구현하기](https://m42-orion.tistory.com/151)

## Github
https://github.com/iniyo/demosignapp