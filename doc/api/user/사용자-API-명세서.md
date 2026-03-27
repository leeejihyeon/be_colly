# 사용자 API 명세서

## 1. 문서 정보
- 도메인: user
- 작성일: 2026-03-27
- 작성자: Codex

## 2. 도메인 개요
- 목적: 사용자 생성 및 단건 조회 제공
- 주요 기능:
  - 사용자 생성
  - 사용자 ID 기반 단건 조회

## 3. 공통 규칙
- Base Path: `/api/users`
- 응답 포맷:
  - 성공 응답은 `ApiResponse<T>` 래퍼를 사용한다.
  - 아래 `Response Body` 예시는 `ApiResponse.data` 내부 payload 기준으로 작성한다.
- 공통 에러 코드는 [공통-API-명세서](/Users/lee/IdeaProjects/colly/doc/api/common/공통-API-명세서.md)를 따른다.

## 4. 엔드포인트 목록
| 구분 | Method | Path | 설명 |
|---|---|---|---|
| 사용자 | POST | `` | 사용자 생성 |
| 사용자 | GET | `/{id}` | 사용자 단건 조회 |

## 5. 상세 명세
### 5.1 사용자 생성
- Method: `POST`
- Path: `/api/users`
- 설명: 이메일/이름으로 사용자를 생성한다.

#### Request Body
```json
{
  "email": "hello@example.com",
  "name": "Lee"
}
```

#### Request Description
| Field | Type | Required | Description |
|---|---|---|---|
| email | String | Y | 사용자 이메일 (`@Email`, `@NotBlank`) |
| name | String | Y | 사용자 이름 (`@NotBlank`) |

#### Response Body (201)
```json
{
  "id": 1,
  "email": "hello@example.com",
  "name": "Lee"
}
```

#### Response Description
| Field | Type | Description |
|---|---|---|
| id | Number | 사용자 식별자 |
| email | String | 사용자 이메일 |
| name | String | 사용자 이름 |

#### Error Response
| Code | Message | Description |
|---|---|---|
| DUPLICATE_EMAIL | 이미 사용 중인 이메일 | 중복 이메일 사용자 생성 시도 |
| VALIDATION_ERROR | fieldName 에러메시지 | 요청 필드 검증 실패 |

### 5.2 사용자 단건 조회
- Method: `GET`
- Path: `/api/users/{id}`
- 설명: 사용자 ID로 단건 조회한다.

#### Path Variable
| Name | Type | Required | Description |
|---|---|---|---|
| id | Number | Y | 사용자 식별자 |

#### Response Body (200)
```json
{
  "id": 1,
  "email": "hello@example.com",
  "name": "Lee"
}
```

#### Error Response
| Code | Message | Description |
|---|---|---|
| USER_NOT_FOUND | 사용자를 찾을 수 없음 | 존재하지 않는 사용자 ID 조회 |

## 6. 비즈니스 규칙
- 이메일은 정규화(공백 제거, 소문자) 후 저장한다.
- 이메일 형식이 올바르지 않으면 생성에 실패한다.
- 동일 이메일은 중복 생성할 수 없다.

## 7. 이력
- v0.1: 사용자 생성/조회 API 문서 최초 작성
