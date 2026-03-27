# 인증 API 명세서

## 1. 문서 정보
- 도메인: auth
- 작성일: 2026-03-26
- 작성자: Codex

## 2. 도메인 개요
- 목적: 이메일 매직링크 기반 로그인 제공
- 주요 기능:
  - 매직링크 발급 요청
  - 매직링크 검증 후 로그인 토큰 발급
  - 소셜 로그인/회원가입(구글/애플/페이스북)

## 3. 공통 규칙
- Base Path: `/api/auth`
- 인증 방식: 로그인 전 API
- 응답 포맷:
  - 성공 응답은 `ApiResponse<T>` 래퍼를 사용한다.
  - 아래 `Response Body` 예시는 `ApiResponse.data` 내부 payload 기준으로 작성한다.
- 공통 에러 코드:
  - `AUTH_INVALID_EMAIL`
  - `AUTH_INVALID_PROVIDER`
  - `AUTH_INVALID_PROVIDER_USER_ID`
  - `MAGIC_LINK_RATE_LIMITED`
  - `MAGIC_LINK_NOT_FOUND`
  - `MAGIC_LINK_EXPIRED`
  - `MAGIC_LINK_ALREADY_USED`

## 4. 엔드포인트 목록
| 구분 | Method | Path | 설명 |
|---|---|---|---|
| 인증 | POST | `/magic-link/request` | 이메일로 매직링크 발급 요청 |
| 인증 | POST | `/magic-link/verify` | 매직링크 토큰 검증 후 로그인 |
| 인증 | POST | `/social/login` | 소셜 로그인/회원가입 후 로그인 |

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
  "expiresInSeconds": 900,
  "magicToken": "발급된-토큰"
}
```

#### Response Description
| Field | Type | Description |
|---|---|---|
| email | String | 정규화된 이메일 |
| expiresInSeconds | Number | 매직링크 만료 시간(초) |
| magicToken | String | 검증용 1회 토큰 |

#### Error Response
| Code | Message | Description |
|---|---|---|
| AUTH_INVALID_EMAIL | Email format is invalid | 이메일 형식 오류 |
| MAGIC_LINK_RATE_LIMITED | Magic link requested too frequently | 60초 이내 재요청 제한 |

### 5.2 매직링크 검증
- Method: `POST`
- Path: `/api/auth/magic-link/verify`
- 설명: 토큰 유효성 검증 후 로그인 세션을 발급한다.

#### Request Body
```json
{
  "token": "발급된-토큰"
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
  "accessToken": "access-token",
  "refreshToken": "refresh-token"
}
```

#### Response Description
| Field | Type | Description |
|---|---|---|
| userId | Number | 로그인한 사용자 ID |
| email | String | 사용자 이메일 |
| accessToken | String | 액세스 토큰 |
| refreshToken | String | 리프레시 토큰 |

#### Error Response
| Code | Message | Description |
|---|---|---|
| MAGIC_LINK_NOT_FOUND | Magic link not found | 토큰 조회 실패 |
| MAGIC_LINK_EXPIRED | Magic link expired | 만료된 토큰 |
| MAGIC_LINK_ALREADY_USED | Magic link already used | 이미 사용된 토큰 |

### 5.3 소셜 로그인/회원가입
- Method: `POST`
- Path: `/api/auth/social/login`
- 설명: 소셜 제공자 계정으로 로그인한다. 최초 로그인 시 자동 회원가입/연결을 수행한다.

#### Request Body
```json
{
  "provider": "GOOGLE",
  "providerUserId": "google-sub-12345",
  "email": "hello@example.com",
  "name": "Lee"
}
```

#### Request Description
| Field | Type | Required | Description |
|---|---|---|---|
| provider | String | Y | 소셜 제공자(`GOOGLE`, `APPLE`, `FACEBOOK`) |
| providerUserId | String | Y | 제공자 내 사용자 고유 식별자 |
| email | String | Y | 사용자 이메일 |
| name | String | N | 사용자 표시 이름(없으면 이메일 기반 기본값 사용) |

#### Response Body (200)
```json
{
  "userId": 10,
  "email": "hello@example.com",
  "accessToken": "access-token",
  "refreshToken": "refresh-token"
}
```

#### Error Response
| Code | Message | Description |
|---|---|---|
| AUTH_INVALID_PROVIDER | Unsupported provider | 지원하지 않는 소셜 제공자 |
| AUTH_INVALID_PROVIDER_USER_ID | Provider user id is required | 제공자 사용자 식별자 누락 |
| AUTH_INVALID_EMAIL | Email format is invalid | 이메일 형식 오류 |

## 6. 비즈니스 규칙
- 매직링크 만료 시간은 `15분(900초)`이다.
- 동일 이메일은 `60초` 이내 재요청 시 차단한다.
- 매직링크는 `1회 사용` 후 재사용할 수 없다.
- 소셜 로그인은 `provider + providerUserId`를 기준으로 계정을 식별한다.
- 최초 소셜 로그인 시 계정이 없으면 자동 회원가입 후 인증 수단을 연결한다.

## 7. 이력
- v0.1: 매직링크 요청/검증 API 최초 작성
- v0.2: 소셜 로그인/회원가입 API 추가
