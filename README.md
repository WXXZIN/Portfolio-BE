# Portfolio Back-end

## 📝 개요

이 프로젝트는 개인 포트폴리오 작품으로, 태그 기반의 인원 모집 앱을 개발한 프로젝트입니다. 사용자는 자신이 필요한 인원과 조건을 태그로 설정하여 모집할 수 있고, 모집에 참여하고자 하는 사람들은 해당 태그를 기반으로 자신에게 맞는 모집 글을 쉽게 찾을 수 있습니다. 이를 통해 보다 효율적으로 팀을 구성하고, 필요한 사람들을 모집할 수 있는 환경을 제공합니다.

<br />

## 🗂️ APIs
작성한 API는 아래에서 확인할 수 있습니다.

👉🏻 [API 바로보기](https://api.wxxzin.org/swagger-ui/index.html#/)

<br />

## 🛠 기술 스택

![Nginx](https://img.shields.io/badge/Nginx-009639?logo=nginx&logoColor=white&style=for-the-badge)
![Ubuntu](https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=Ubuntu&logoColor=white)
![Docker](https://img.shields.io/badge/docker-257bd6?style=for-the-badge&logo=docker&logoColor=white)

<br />

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=Spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_data_jpa-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

<br />

### 프로젝트 아키텍처
![](https://github.com/user-attachments/assets/a7aed734-289f-4125-b4d2-b8ed87d24ca2)

<br />

## 💡 주요 기능
### 사용자 인증  
- **일반 회원 가입**: 이메일 인증을 통한 회원 가입  
- **소셜 로그인**: 클라이언트에서 SDK를 통해 로그인 후, OAuth2를 이용하여 사용자 정보 검증 및 처리  
- **JWT 인증**: 자체 액세스 토큰 & 리프레시 토큰 발급 및 검증  
- **토큰 관리**: Redis를 사용하여 JWT 저장 및 만료 관리 (TTL 설정으로 자동 삭제)  

### 스케줄링  
- **만료된 리프레시 토큰 정리** (Redis TTL을 사용해 자동 삭제)  
- 기타 예약된 백엔드 작업 실행  

### 데이터베이스 연동  
- **JPA**: 객체지향적인 데이터베이스 접근을 위한 JPA 사용  
- **MySQL**: 관계형 데이터베이스 관리 시스템  

### API 문서화  
- **Swagger**: API 문서 자동화 및 테스트 가능  

### 환경 변수 관리  
- **.env 파일**: OAuth2, DB 정보, Redis 설정 등 보안이 필요한 값 관리  

### 보안  
- **Spring Security 적용**: 보안 설정을 통한 인증 및 권한 관리 

<br />

## 💻 실행 방법

### 1. **설치**

```bash
$ git clone https://github.com/WXXZIN/Portfolio-BE.git
```

### 2. **.env 작성**
src/main/resources 경로에 .env 파일 생성

```bash
# 테스트
OAUTH_SUCCESS_REDIRECT_URI=http://localhost:3000/auth/oauth2/success

GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=

KAKAO_CLIENT_ID=
KAKAO_CLIENT_SECRET=

NAVER_CLIENT_ID=
NAVER_CLIENT_SECRET=

DB_HOST=
DB_PORT=
DB_NAME=
DB_USER=
DB_PASSWORD=

REDIS_HOST=
REDIS_PORT=
REDIS_PASSWORD=
REDIS_DEVICE_INDEX=0
REDIS_JWT_INDEX=1
REDIS_MAIL_INDEX=2

MAIL_HOST=
MAIL_PORT=
MAIL_USERNAME=
MAIL_PASSWORD=

JWT_SECRET=
```
