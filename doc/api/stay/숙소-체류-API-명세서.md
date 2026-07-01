# 숙소/체류 API 명세서

## 1. 문서 정보
- 도메인: stay
- 작성일: 2026-07-01
- 작성자: Codex

## 2. 도메인 개요
- 목적: 숙소 목록 조회, 내 체류 일정 관리, 같은 숙소 기간 겹침 사용자 조회
- 주요 기능:
  - 도시 기준 숙소 목록 조회
  - 내 체류 일정 등록/조회/수정/삭제
  - 동일 숙소 + 기간 겹침 사용자 조회

## 3. 공통 규칙
- Base Path: `/api`
- 인증 방식:
  - `GET /accommodations`: 비로그인 허용
  - `/stays/**`: `Authorization: Bearer <accessToken>` 필수
- 응답 포맷:
  - 성공 응답은 `ApiResponse<T>` 래퍼를 사용한다.
- 공개 정보 최소화:
  - overlap 응답은 `userId`, `nickname`, `overlapStartDate`, `overlapEndDate`만 반환한다.

## 4. 엔드포인트 목록
| 구분 | Method | Path | 설명 |
|---|---|---|---|
| 숙소 | GET | `/accommodations` | 국가/도시 기준 숙소 목록 조회 |
| 체류 | POST | `/stays` | 내 체류 일정 등록 |
| 체류 | GET | `/stays/me` | 내 체류 일정 목록 조회 |
| 체류 | PUT | `/stays/{stayId}` | 내 체류 일정 수정 |
| 체류 | DELETE | `/stays/{stayId}` | 내 체류 일정 삭제 |
| 체류 | GET | `/stays/{stayId}/overlaps` | 내 체류 일정 기준 겹침 사용자 조회 |

## 5. 상세 명세
### 5.1 숙소 목록 조회
- Method: `GET`
- Path: `/api/accommodations?countryCode=AU&cityCode=SYD`

#### Response Body (200)
```json
[
  {
    "id": 1001,
    "countryCode": "AU",
    "cityCode": "SYD",
    "name": "Wake Up Sydney",
    "addressLine1": "509 Pitt St",
    "addressLine2": null
  }
]
```

### 5.2 체류 일정 등록
- Method: `POST`
- Path: `/api/stays`

#### Request Body
```json
{
  "accommodationId": 1001,
  "checkIn": "2026-07-10",
  "checkOut": "2026-07-20"
}
```

#### Response Body (201)
```json
{
  "id": 1,
  "accommodationId": 1001,
  "checkIn": "2026-07-10",
  "checkOut": "2026-07-20",
  "createdAt": "2026-07-01T10:30:00"
}
```

### 5.3 내 체류 일정 목록 조회
- Method: `GET`
- Path: `/api/stays/me`

### 5.4 체류 일정 수정
- Method: `PUT`
- Path: `/api/stays/{stayId}`
- Request Body는 등록과 동일

### 5.5 체류 일정 삭제
- Method: `DELETE`
- Path: `/api/stays/{stayId}`

### 5.6 겹침 사용자 조회
- Method: `GET`
- Path: `/api/stays/{stayId}/overlaps`

#### Response Body (200)
```json
[
  {
    "userId": 9,
    "nickname": "Mina",
    "overlapStartDate": "2026-07-15",
    "overlapEndDate": "2026-07-20"
  }
]
```

## 6. 비즈니스 규칙
- `checkIn <= checkOut` 이어야 한다.
- 같은 사용자의 동일 숙소 + 동일 체크인/체크아웃 일정은 중복 등록할 수 없다.
- 수정/삭제/overlap 조회는 본인 일정만 가능하다.
- overlap은 같은 숙소이면서 날짜가 교차하는 경우에만 반환한다.
- overlap 조회 결과에서 자기 자신은 제외한다.

## 7. 에러 코드
- `ACCOMMODATION_NOT_FOUND`
- `STAY_NOT_FOUND`
- `INVALID_STAY_DATE_RANGE`
- `DUPLICATE_STAY`
- `FORBIDDEN_ACTION`
- `AUTH_UNAUTHORIZED`
- `AUTH_INVALID_ACCESS_TOKEN`
