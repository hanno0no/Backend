# hnn (Backend)

행사 현장용 **주문·진행 현황 백엔드** (Spring Boot REST API).

경상북도교육청 주관 중·고등학생 해커톤에서 레이저 커팅/3D 프린팅 제작 의뢰를 접수·관리하고, 현장 대시보드에 진행 현황을 실시간으로 공개하는 [hnn-react](https://github.com/hanno0no/Frontend) 프론트엔드의 API 서버입니다.

프론트엔드·API 계약 상세는 [hnn-react/API.md](https://github.com/hanno0no/Frontend/blob/refactoring/API.md)를 참고하세요.

---

## 기술 스택

| 항목 | 버전 |
|------|------|
| Java | 21 |
| Spring Boot | 3.5.3 |
| Spring Data JPA / Spring Security | (Boot BOM) |
| JWT | jjwt 0.11.5 |
| DB | MariaDB |
| 빌드 | Gradle |

---

## API 엔드포인트

모든 경로는 `/hnn`으로 시작합니다.

### 공개 (인증 불필요)

| Method | 경로 | 설명 |
|---|---|---|
| GET | `/hnn/index` | 메인 대시보드 (완료/대기 명단, 종료 시각, 공지) |
| GET | `/hnn/events` | 대시보드·관리자 실시간 갱신용 SSE 스트림 |
| POST | `/hnn/register` (`/registar`도 병행 지원) | 접수 등록, 접수번호 발급 |
| GET | `/hnn/register/getmaterial` | 활성 재질 목록 |
| GET | `/hnn/register/getstate` | 상태 코드 목록 |
| GET | `/hnn/register/getadminname` | 담당자(관리자) 이름 목록 |
| GET | `/hnn/checkStatus?teamNum=` | 팀명으로 본인 주문 진행 상황 조회 |

### 관리자 (JWT 필요)

| Method | 경로 | 설명 |
|---|---|---|
| POST | `/hnn/admin/login` | 로그인, JWT 발급 |
| POST | `/hnn/admin/signup` | 관리자 계정 생성 |
| GET | `/hnn/admin/view` | 주문 목록 조회 (상태·담당자·재질·팀 필터) |
| PATCH | `/hnn/admin/{orderId}/status` | 주문 상태 변경 |
| PATCH | `/hnn/admin/{orderId}/manager` | 담당자 배정/해제 |
| PATCH | `/hnn/admin/{orderId}/hide` | 완료 명단에서 숨기기/다시 표시 |
| GET | `/hnn/admin/stats` | 상태별 건수 + 시간대별 접수/상태 추이 |
| GET / PATCH | `/hnn/admin/setting` | 이벤트·공지·재질·대시보드 표시 개수 설정 |
| POST | `/hnn/admin/create/{material\|message\|eventinfo}` | 재질·공지·이벤트 생성 |
| DELETE | `/hnn/admin/delete/{material\|message\|eventinfo}/{id}` | 재질·공지·이벤트 삭제 |

`/hnn/admin/**`은 `SecurityConfig`에서 `hasRole("admin")`으로 보호되며, `JwtAuthenticationFilter`가 `Authorization: Bearer <token>` 헤더를 검증합니다.

---

## 패키지 구조

```
hanno0no.hnn
├── controller/   # index, register, checkStatus, admin, sse
├── service/      # 컨트롤러별 비즈니스 로직
├── repository/    # Spring Data JPA
├── domain/       # Orders, Team, Material, State, AdminUser, EventInfo, Message
├── request/      # 요청 DTO
├── response/     # 응답 DTO
├── config/       # SecurityConfig, WebConfig(CORS)
├── security/     # JwtAuthenticationFilter, JwtTokenProvider
└── exception/    # GlobalExceptionHandler, 커스텀 예외
```

핵심 도메인은 `Orders`(주문 1건) — `Team`·`Material`·`State`·`AdminUser`를 참조하고, `updated_at`은 `@UpdateTimestamp`로 상태 변경 시각을 자동 기록합니다. 완료 명단·통계의 시간대별 추이는 이 `updated_at`(또는 `ordered_at`)을 기준으로 집계합니다.

---

## 로컬 실행

```bash
./gradlew bootRun
```

DB 접속 정보와 JWT 시크릿은 환경 변수로 주입합니다 (`application.yml` 참고).

| 변수 | 설명 |
|---|---|
| `SPRING_DATASOURCE_URL` | JDBC URL (예: `jdbc:mariadb://localhost:3306/hnn`) |
| `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD` | DB 계정 |
| `JWT_SECRET_KEY` | JWT 서명 키 (32바이트 이상 임의 문자열) |
| `JWT_EXPIRATION_TIME` | 토큰 만료(ms), 미설정 시 12시간 |

> **주의**: `application.properties`가 `application.yml`보다 우선 적용되는데, 현재 `application.properties`에 팀 공용 개발 DB 접속 정보가 하드코딩되어 있습니다. 별도 DB로 실행하려면 위 환경 변수를 설정한 뒤 `application.properties`의 값을 비우거나 지우고 실행하세요. (커밋되어 있는 값이라 `.gitignore` 처리나 환경 변수 전환이 필요한 부분입니다.)

DB 스키마·마이그레이션 SQL은 이 저장소에 포함되어 있지 않습니다 — 이미 구성된 DB에 연결하거나, 팀 공유 DB 접속 정보로 실행하세요.

### 배포

CORS 허용 origin은 `config/WebConfig.java`에 고정되어 있으므로, 배포 도메인이 바뀌면 코드에서 함께 수정해야 합니다.

---

## 관련 저장소

- [hnn-react](https://github.com/hanno0no/Frontend) — 프론트엔드 (React + Vite)
