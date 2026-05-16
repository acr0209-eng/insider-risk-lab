# Insider Risk Lab

Spring Boot + MySQL 기반의 비악의적 내부자 위협 시나리오 실험 플랫폼입니다.

이 프로젝트는 업무상 동기와 보안위반 기회가 내부자료 반출 의향에 미치는 영향을 2×2 반복측정 설문으로 수집하고, 관리자 대시보드에서 조건별 평균을 확인할 수 있도록 설계되었습니다.

## 주요 기능

- 한 참여자가 A/B/C/D 4개 시나리오를 모두 응답
- 참여자별 시나리오 제시 순서 랜덤화
- 응답자 화면에서 조건 코드, Motivation, Opportunity 정보 숨김
- Likert 5점 척도 설문 수집
- 보안위반 의향, 정당화 수준, 보안 인식 점수 자동 계산
- 관리자 대시보드 로그인 보호
- 관리자 대시보드에서 참여자 수, 전체 응답 수, 조건별 평균 확인
- Chart.js 기반 그래프
- CSV 다운로드

## 기술 스택

- Java 21 권장
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- Spring Security
- Thymeleaf
- MySQL
- Chart.js CDN
- Gradle

## 실행 전 준비

MySQL에서 데이터베이스를 먼저 생성합니다.

```sql
CREATE DATABASE insider_risk_lab CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

로컬에서는 `src/main/resources/application.properties`의 fallback 값을 본인 환경에 맞게 수정하거나, 환경변수로 설정합니다.

```properties
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:root}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:PUT_YOUR_MYSQL_PASSWORD_HERE}
```

관리자 기본 계정은 로컬 개발용입니다.

```properties
ADMIN_USERNAME=${ADMIN_USERNAME:admin}
ADMIN_PASSWORD=${ADMIN_PASSWORD:admin1234}
```

배포 환경에서는 반드시 `ADMIN_USERNAME`, `ADMIN_PASSWORD`를 Railway Variables로 설정하세요.

## 실행 방법

```bash
gradle bootRun
```

또는 IntelliJ IDEA에서 `InsiderRiskLabApplication.java`를 실행합니다.

## 접속 경로

- 홈: `http://localhost:8080/`
- 설문 시작: `http://localhost:8080/survey/start`
- 관리자 대시보드: `http://localhost:8080/admin/dashboard`
- CSV 다운로드: `http://localhost:8080/admin/responses.csv`

## 연구 설계

| 조건 | 업무상 동기 | 보안위반 기회 | 설명 |
|---|---|---|---|
| A | 높음 | 높음 | 마감 압박이 크고 자료 반출이 쉬운 상황 |
| B | 높음 | 낮음 | 마감 압박은 크지만 보안통제가 강한 상황 |
| C | 낮음 | 높음 | 업무 필요성은 낮지만 자료 반출이 쉬운 상황 |
| D | 낮음 | 낮음 | 업무 필요성도 낮고 보안통제도 강한 상황 |

## Railway 배포 변수

Spring 앱 서비스 Variables에 아래 값을 설정합니다.

```env
SPRING_DATASOURCE_URL=jdbc:mysql://실제호스트:실제포트/실제DB명?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
SPRING_DATASOURCE_USERNAME=실제유저
SPRING_DATASOURCE_PASSWORD=실제비밀번호
ADMIN_USERNAME=admin
ADMIN_PASSWORD=강한관리자비밀번호
```

## 다음 개선 후보

- 2×2 히트맵 추가
- 정당화 수준과 반출 의향 간 상관분석 추가
- Two-way repeated measures ANOVA 분석 기능 추가
- PDF 보고서 자동 생성
