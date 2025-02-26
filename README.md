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
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
<br />

### 프로젝트 아키텍처
![](https://github.com/user-attachments/assets/a58aaae9-bebe-47f7-8cb4-12f70c2be54a)

<br />

## 💡 주요 기능
### 헬스 체크 및 서버 모니터링
- **Crontab을 이용한 헬스 체크**: 주기적으로 서버 상태를 점검하고, 문제가 발생하면 자동으로 Slack을 통해 알림 전달

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

### 데이터 캐싱 및 세션 관리
- **Redis**: 이메일 인증번호, JWT 토큰 등을 저장  
- **Redis Sentinel을 이용한 장애 복구**: Redis Sentinel을 활용하여 장애 발생 시 자동으로 마스터를 변경하여 가용성을 유지 

### API 문서화  
- **Swagger**: API 문서 자동화 및 테스트 가능  

### 환경 변수 관리  
- **.env 파일**: DB 정보, Redis 설정 등 보안이 필요한 값 관리  

### 보안  
- **Spring Security 적용**: 보안 설정을 통한 인증 및 권한 관리 

<br />

## 💻 실행 방법

### 1. **설치**

```bash
$ git clone https://github.com/WXXZIN/Portfolio-BE.git
```

<br />

### 2. **DB 볼륨 디렉토리 생성**

```bash
$ mkdir -p mysql/conf.d
$ mkdir -p mysql/data
$ mkdir -p mysql/init

$ touch mysql/conf.d/mysqld.cnf

$ mkdir -p redis/data

$ sudo chmod -R 777 mysql/data mysql/init mysql/conf.d
$ sudo chmod -R 777 redis/data
```

<br />

### 3. **Docker Compose 실행**
`docker-compose.yml` 파일을 통해 서비스를 구성할 수 있습니다. 자세한 내용은 [docker-compose.yml](https://bony-spell-7d4.notion.site/Portfolio-DB-docker-compose-1a67f240b10580e98446f1251ec3de3b)을 참고하세요.

| 번호  | 변수 설명                      | 예시 값|
|------|------------------------------|--------------------|
| **[1]**  | MySQL 호스트 포트              | `3306` |
| **[2]**  | MySQL 컨테이너 포트           | `3306` |
| **[3]**  | MySQL 사용자 계정             | `myuser` |
| **[4]**  | MySQL 사용자 비밀번호         | `mypassword` |
| **[5]**  | MySQL Root 비밀번호          | `rootpassword` |
| **[6]**  | Redis 마스터 비밀번호         | `redismasterpass` |
| **[7]**  | Redis 일반 비밀번호           | `redispassword` |
| **[8]**  | 내부 IP 주소                 | `192.168.0.2` |
| **[9]**  | Redis Sentinel 비밀번호       | `sentinelpass` |
| **[10]** | Redis Sentinel 1 호스트 포트  | `26379` |
| **[11]** | Redis Sentinel 1 컨테이너 포트 | `26379` |
| **[12]** | Redis Sentinel 2 호스트 포트  | `26380` |
| **[13]** | Redis Sentinel 2 컨테이너 포트 | `26379` |
| **[14]** | Redis Sentinel 3 호스트 포트  | `26381` |
| **[15]** | Redis Sentinel 3 컨테이너 포트 | `26379` |

<br />

### 4. **.env 작성**
src/main/resources 경로에 .env 파일 생성

```bash
PORT=

DB_HOST=
DB_PORT=
DB_NAME=
DB_USER=
DB_PASSWORD=

REDIS_JWT_INDEX=0
REDIS_MAIL_INDEX=1

REDIS_SENTINEL_MASTER=
REDIS_SENTINEL_PASSWORD=

MAIL_HOST=
MAIL_PORT=
MAIL_USERNAME=
MAIL_PASSWORD=

JWT_SECRET=
```

<br />

### 5. **RedisConfig 수정**
```bash
package com.wxxzin.portfolio.server.common.config;

@Value("${spring.data.redis.sentinel.master}")
private String masterName;

@Value("${spring.data.redis.sentinel.password}")
private String sentinelPassword;

private RedisSentinelConfiguration createSentinelConfig() {
        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration()
                .master(masterName);

        redisSentinelConfiguration.sentinel("[8]", [10]);
        redisSentinelConfiguration.sentinel("[8]", [12]);
        redisSentinelConfiguration.sentinel("[8]", [14]);
        redisSentinelConfiguration.setSentinelPassword(sentinelPassword);
        redisSentinelConfiguration.setPassword(sentinelPassword);

        return redisSentinelConfiguration;
    }
```
