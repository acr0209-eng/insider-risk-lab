# Insider Risk Lab

Spring Boot + MySQL 기반의 비악의적 내부자 위협 시나리오 실험 플랫폼입니다.

이 프로젝트는 업무상 동기와 보안위반 기회가 내부자료 반출 의향에 미치는 영향을 2×2 시나리오 실험으로 수집하고, 관리자 대시보드에서 조건별 평균을 확인할 수 있도록 설계되었습니다.

## 주요 기능

- A/B/C/D 4개 시나리오 랜덤 배정
- Likert 5점 척도 설문 수집
- 보안위반 의향, 정당화 수준, 보안 인식 점수 자동 계산
- 관리자 대시보드에서 시나리오별 평균 확인
- Chart.js 기반 막대그래프
- CSV 다운로드

## 기술 스택

- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- Thymeleaf
- MySQL
- Chart.js CDN
- Gradle

## 실행 전 준비

MySQL에서 데이터베이스를 먼저 생성합니다.

```sql
CREATE DATABASE insider_risk_lab CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

`src/main/resources/application.properties`에서 MySQL 비밀번호를 본인 환경에 맞게 수정합니다.

```properties
spring.datasource.username=root
spring.datasource.password=PUT_YOUR_MYSQL_PASSWORD_HERE
```

## 실행 방법

```bash
./gradlew bootRun
```

Windows PowerShell에서 Gradle Wrapper가 없으면 로컬 Gradle 설치 후 아래 명령을 사용할 수 있습니다.

```bash
gradle bootRun
```

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

## 다음 개선 후보

- 관리자 로그인 추가
- 2×2 히트맵 추가
- 정당화 수준과 반출 의향 간 상관분석 추가
- Two-way ANOVA 분석 기능 추가
- PDF 보고서 자동 생성
