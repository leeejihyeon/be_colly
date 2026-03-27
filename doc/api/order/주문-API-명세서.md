# 주문 API 명세서

## 1. 문서 정보
- 도메인: order
- 작성일: 2026-03-27
- 작성자: Codex

## 2. 도메인 개요
- 목적: 주문 생성 및 단건 조회 제공
- 주요 기능:
  - 주문 생성
  - 주문 ID 기반 단건 조회

## 3. 공통 규칙
- Base Path: `/api/orders`
- 응답 포맷:
  - 성공 응답은 `ApiResponse<T>` 래퍼를 사용한다.
  - 아래 `Response Body` 예시는 `ApiResponse.data` 내부 payload 기준으로 작성한다.
- 공통 에러 코드는 [공통-API-명세서](/Users/lee/IdeaProjects/colly/doc/api/common/공통-API-명세서.md)를 따른다.

## 4. 엔드포인트 목록
| 구분 | Method | Path | 설명 |
|---|---|---|---|
| 주문 | POST | `` | 주문 생성 |
| 주문 | GET | `/{id}` | 주문 단건 조회 |

## 5. 상세 명세
### 5.1 주문 생성
- Method: `POST`
- Path: `/api/orders`
- 설명: 주문자 사용자 ID와 금액으로 주문을 생성한다.

#### Request Body
```json
{
  "userId": 1,
  "amount": 100.50
}
```

#### Request Description
| Field | Type | Required | Description |
|---|---|---|---|
| userId | Number | Y | 주문자 사용자 ID (`@NotNull`) |
| amount | Number | Y | 주문 금액 (`@NotNull`, `@DecimalMin("0.01")`) |

#### Response Body (201)
```json
{
  "id": 1,
  "userId": 1,
  "amount": 100.50
}
```

#### Response Description
| Field | Type | Description |
|---|---|---|
| id | Number | 주문 식별자 |
| userId | Number | 주문자 사용자 식별자 |
| amount | Number | 주문 금액 |

#### Error Response
| Code | Message | Description |
|---|---|---|
| INVALID_AMOUNT | 유효하지 않은 금액 | 0 이하 금액 주문 시도 |
| VALIDATION_ERROR | fieldName 에러메시지 | 요청 필드 검증 실패 |

### 5.2 주문 단건 조회
- Method: `GET`
- Path: `/api/orders/{id}`
- 설명: 주문 ID로 단건 조회한다.

#### Path Variable
| Name | Type | Required | Description |
|---|---|---|---|
| id | Number | Y | 주문 식별자 |

#### Response Body (200)
```json
{
  "id": 1,
  "userId": 1,
  "amount": 100.50
}
```

#### Error Response
| Code | Message | Description |
|---|---|---|
| ORDER_NOT_FOUND | 주문을 찾을 수 없음 | 존재하지 않는 주문 ID 조회 |

## 6. 비즈니스 규칙
- 주문 금액은 `0`보다 커야 한다.
- 주문 조회 시 존재하지 않는 ID는 `ORDER_NOT_FOUND` 오류로 처리한다.

## 7. 이력
- v0.1: 주문 생성/조회 API 문서 최초 작성
