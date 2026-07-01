# 인증 API 명세서

## 1. 문서 정보
- 도메인: auth
- 작성일: 2026-03-26
- 최종 수정일: 2026-06-29
- 작성자: Codex

## 2. 도메인 개요
- 목적: 이메일 매직링크, Google, Apple 기반 로그인 및 세션 인증 제공
- 주요 기능:
  - 매직링크 발급 요청
  - 매직링크 검증 후 로그인 토큰 발급
  - Google / Apple 로그인
  - 액세스 토큰 갱신
  - 현재 사용자 조회
  - 현재 세션/전체 세션 로그아웃

## 3. 공통 규칙
- Base Path: `/api/auth`
- 보호 API 인증 방식: `Authorization: Bearer <accessToken>`
- 응답 포맷:
  - 성공 응답은 `ApiResponse<T>` 래퍼를 사용한다.
  - 아래 `Response Body` 예시는 `ApiResponse.data` 내부 payload 기준으로 작성한다.
- 공통 에러 코드:
  - `AUTH_INVALID_EMAIL`
  - `AUTH_INVALID_ACCESS_TOKEN`
  - `AUTH_SOCIAL_TOKEN_INVALID`
  - `AUTH_UNAUTHORIZED`
  - `MAGIC_LINK_RATE_LIMITED`
  - `MAGIC_LINK_NOT_FOUND`
  - `MAGIC_LINK_EXPIRED`
  - `MAGIC_LINK_ALREADY_USED`
  - `REFRESH_TOKEN_NOT_FOUND`
  - `REFRESH_TOKEN_EXPIRED`

## 4. 엔드포인트 목록
| 구분 | Method | Path | 설명 |
|---|---|---|---|
| 인증 | POST | `/magic-link/request` | 이메일로 매직링크 발급 요청 |
| 인증 | POST | `/magic-link/verify` | 매직링크 토큰 검증 후 로그인 |
| 인증 | GET | `/magic-link/verify` | 메일 링크 fallback용 매직링크 검증 |
| 인증 | POST | `/token/refresh` | 리프레시 토큰으로 액세스/리프레시 토큰 갱신 |
| 인증 | POST | `/signout` | 현재 리프레시 토큰 세션 로그아웃 |
| 인증 | POST | `/signout-all` | 현재 로그인 사용자의 전체 세션 로그아웃 |
| 인증 | GET | `/me` | 현재 로그인 사용자 조회 |
| 인증 | POST | `/social/google` | Google 로그인/회원가입 |
| 인증 | POST | `/social/apple` | Apple 로그인/회원가입 |

## 5. 상세 명세
### 5.1 매직링크 발급
- Method: `POST`
- Path: `/api/auth/magic-link/request`
- 설명: 이메일 유효성 검증 후 매직링크 토큰을 발급한다.

#### Request Body
```json
{
  "email": "hello@example.com"
}
```

#### Request Description
| Field | Type | Required | Description |
|---|---|---|---|
| email | String | Y | 로그인할 이메일 주소 |

#### Response Body (201)
```json
{
  "email": "hello@example.com",
  "expiresInSeconds": 900
}
```

#### Response Description
| Field | Type | Description |
|---|---|---|
| email | String | 정규화된 이메일 |
| expiresInSeconds | Number | 매직링크 만료 시간(초) |

#### 메일 템플릿 링크 정책
- 앱 링크 우선: `collyapp://auth/magic-link?token=...`
- 웹 fallback: `/api/auth/magic-link/verify?token=...`

#### Error Response
| Code | Message | Description |
|---|---|---|
| AUTH_INVALID_EMAIL | Email format is invalid | 이메일 형식 오류 |
| MAGIC_LINK_RATE_LIMITED | Magic link requested too frequently | 60초 이내 재요청 제한 |
| MAGIC_LINK_EMAIL_SEND_FAILED | Failed to send magic link email | SMTP 발송 실패 |

### 5.2 매직링크 검증
- Method: `POST`
- Path: `/api/auth/magic-link/verify`
- 설명: 토큰 유효성 검증 후 로그인 세션을 발급한다.

#### Request Body
```json
{
  "token": "issued-magic-link-token"
}
```

#### Request Description
| Field | Type | Required | Description |
|---|---|---|---|
| token | String | Y | 매직링크 요청 시 발급된 토큰 |

#### Response Body (200)
```json
{
  "userId": 10,
  "email": "hello@example.com",
  "accessToken": "jwt-access-token",
  "refreshToken": "refresh-token",
  "expiresInSeconds": 900,
  "refreshExpiresInSeconds": 2592000,
  "provider": "EMAIL_MAGIC_LINK"
}
```

#### Response Description
| Field | Type | Description |
|---|---|---|
| userId | Number | 로그인한 사용자 ID |
| email | String | 사용자 이메일 |
| accessToken | String | JWT 액세스 토큰 |
| refreshToken | String | 리프레시 토큰 |
| expiresInSeconds | Number | 액세스 토큰 만료 시간(초) |
| refreshExpiresInSeconds | Number | 리프레시 토큰 만료 시간(초) |
| provider | String | 인증 수단(`EMAIL_MAGIC_LINK`, `GOOGLE`, `APPLE`) |

#### Error Response
| Code | Message | Description |
|---|---|---|
| MAGIC_LINK_NOT_FOUND | Magic link not found | 토큰 조회 실패 |
| MAGIC_LINK_EXPIRED | Magic link expired | 만료된 토큰 |
| MAGIC_LINK_ALREADY_USED | Magic link already used | 이미 사용된 토큰 |

### 5.3 매직링크 링크 fallback 검증
- Method: `GET`
- Path: `/api/auth/magic-link/verify?token=...`
- 설명: 메일에서 열리는 웹 fallback 경로다. 기본 응답 스키마는 `POST /magic-link/verify`와 동일하다.

### 5.4 토큰 갱신
- Method: `POST`
- Path: `/api/auth/token/refresh`
- 설명: 리프레시 토큰으로 액세스/리프레시 토큰을 재발급한다.

#### Request Body
```json
{
  "refreshToken": "refresh-token"
}
```

#### Response Body (200)
```json
{
  "userId": 10,
  "email": "hello@example.com",
  "accessToken": "jwt-access-token",
  "refreshToken": "new-refresh-token",
  "expiresInSeconds": 900,
  "refreshExpiresInSeconds": 2592000,
  "provider": "GOOGLE"
}
```

#### Error Response
| Code | Message | Description |
|---|---|---|
| REFRESH_TOKEN_NOT_FOUND | Refresh token not found | 세션이 만료되었거나 비정상 토큰 |
| REFRESH_TOKEN_EXPIRED | Refresh token expired | 리프레시 토큰 만료 |
| USER_NOT_FOUND | User not found | 사용자 삭제 등 |

### 5.5 현재 세션 로그아웃
- Method: `POST`
- Path: `/api/auth/signout`
- 설명: 전달된 리프레시 토큰 기준으로 현재 세션 하나만 종료한다.

#### Request Body
```json
{
  "refreshToken": "refresh-token"
}
```

#### Response Body (200)
```json
null
```

### 5.6 전체 세션 로그아웃
- Method: `POST`
- Path: `/api/auth/signout-all`
- 인증: `Authorization: Bearer <accessToken>`
- 설명: 현재 로그인 사용자 기준으로 모든 기기 세션을 종료한다.

#### Response Body (200)
```json
null
```

### 5.7 현재 사용자 조회
- Method: `GET`
- Path: `/api/auth/me`
- 인증: `Authorization: Bearer <accessToken>`
- 설명: 현재 로그인 사용자와 인증 제공자 정보를 반환한다.

#### Response Body (200)
```json
{
  "userId": 10,
  "email": "hello@example.com",
  "provider": "GOOGLE"
}
```

### 5.8 Google 로그인/회원가입
- Method: `POST`
- Path: `/api/auth/social/google`
- 설명: Google ID Token을 검증하고 로그인한다. 최초 로그인 시 자동 회원가입/연결을 수행한다.

#### Request Body
```json
{
  "idToken": "google-id-token"
}
```

#### Request Description
| Field | Type | Required | Description |
|---|---|---|---|
| idToken | String | Y | Google Sign-In SDK가 반환한 ID Token |

#### Response Body (200)
```json
{
  "userId": 10,
  "email": "hello@example.com",
  "accessToken": "jwt-access-token",
  "refreshToken": "refresh-token",
  "expiresInSeconds": 900,
  "refreshExpiresInSeconds": 2592000,
  "provider": "GOOGLE"
}
```

### 5.9 Apple 로그인/회원가입
- Method: `POST`
- Path: `/api/auth/social/apple`
- 설명: Apple identity token을 검증하고 로그인한다. 최초 로그인 시 자동 회원가입/연결을 수행한다.

#### Request Body
```json
{
  "identityToken": "apple-identity-token",
  "authorizationCode": "optional-authorization-code",
  "name": "Lee"
}
```

#### Request Description
| Field | Type | Required | Description |
|---|---|---|---|
| identityToken | String | Y | Sign in with Apple이 반환한 identity token |
| authorizationCode | String | N | Apple authorization code |
| name | String | N | 최초 가입 시 표시 이름 후보 |

#### Response Body (200)
```json
{
  "userId": 10,
  "email": "hello@example.com",
  "accessToken": "jwt-access-token",
  "refreshToken": "refresh-token",
  "expiresInSeconds": 900,
  "refreshExpiresInSeconds": 2592000,
  "provider": "APPLE"
}
```

#### Error Response
| Code | Message | Description |
|---|---|---|
| AUTH_SOCIAL_TOKEN_INVALID | Provider token is invalid | provider 토큰 검증 실패 |
| AUTH_UNAUTHORIZED | Authentication is required | 인증 누락 |
| AUTH_INVALID_ACCESS_TOKEN | Access token is invalid or expired | 액세스 토큰 무효/만료 |

## 6. 비즈니스 규칙
- 액세스 토큰은 JWT 기반이며 기본 만료 시간은 `15분(900초)`이다.
- refresh token은 DB 세션 기반이며 기본 만료 시간은 `30일(2592000초)`이다.
- 동일 이메일은 `60초` 이내 재요청 시 차단한다.
- 매직링크는 `1회 사용` 후 재사용할 수 없다.
- 토큰 갱신 시 기존 리프레시 토큰은 즉시 폐기되고 새 토큰이 발급된다.
- 다중 기기 세션은 허용한다.
- `signout`은 현재 refresh 세션만 종료한다.
- `signout-all`은 현재 사용자 기준 모든 refresh 세션을 종료한다.
- 소셜 로그인은 provider token 검증 결과의 `sub`와 `email`을 기준으로 계정을 식별/연결한다.
- Apple relay email은 정상 이메일로 취급한다.

## 7. 실서비스 설정 체크리스트
- `colly.auth.jwt.secret` 준비 및 환경별 분리
- `colly.auth.social.google.audience`에 Google Web Client ID 입력
- `colly.auth.social.apple.audience`에 iOS Bundle ID 입력
- `spring.mail.username`, `spring.mail.password`, `colly.auth.mail.from` 설정
- iOS 앱에 `GoogleService-Info.plist` 추가
- iOS target에 `Sign in with Apple` capability 활성화
- Android debug / release SHA-1, SHA-256를 Google Console에 등록
- 모바일 앱과 서버의 issuer / audience / bundle id / package id가 서로 일치하는지 확인

## 8. 수동 검증 루틴
1. iOS에서 매직링크 요청 후 메일 버튼으로 앱 복귀 확인
2. iOS에서 Google 로그인 시작 및 앱 복귀 확인
3. iOS에서 Apple 로그인 시스템 시트 노출 확인
4. Android에서 Google 로그인 시작 및 앱 복귀 확인
5. 액세스 토큰 만료 후 refresh 자동 갱신 확인
6. `signout` / `signout-all` 이후 보호 API 차단 확인

## 9. 이력
- v0.1: 매직링크 요청/검증 API 최초 작성
- v0.2: 소셜 로그인/회원가입 API 추가
- v0.3: 리프레시 토큰 기반 갱신 및 signout API 추가
- v0.4: JWT 액세스 토큰, `/me`, `/signout-all`, Google/Apple token 검증 방식 반영
