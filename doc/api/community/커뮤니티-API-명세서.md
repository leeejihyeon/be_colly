# 커뮤니티 API 명세서

## 1. 문서 정보
- 도메인: community
- 작성일: 2026-03-26
- 작성자: Codex

## 2. 도메인 개요
- 목적: 도시 기반 커뮤니티 게시글, 모임 참여, 신고/제재 관리
- 주요 기능:
  - 게시글 생성/조회 (`GATHERING`, `FREE_FEED`)
  - 모임 참여 및 참여 승인/거절
  - 신고 누적 기반 7일 제재

## 3. 공통 규칙
- Base Path: `/api/community`
- 인증 방식: 현재 API 파라미터로 사용자 ID를 전달(추후 토큰 인증 연동 예정)
- 게시글 타입:
  - `GATHERING`: 모임 모집형
  - `FREE_FEED`: 자유형 게시글
- 공통 에러 코드:
  - `POST_NOT_FOUND`, `JOIN_NOT_FOUND`, `JOIN_NOT_ALLOWED`, `JOIN_ALREADY_EXISTS`
  - `INVALID_POST_PAYLOAD`, `FORBIDDEN_ACTION`, `USER_RESTRICTED`

## 4. 엔드포인트 목록
| 구분 | Method | Path | 설명 |
|---|---|---|---|
| 게시글 | POST | `/posts` | 게시글 생성 |
| 게시글 | GET | `/posts` | 도시+타입 기준 피드 조회 |
| 모임 | POST | `/posts/{postId}/join` | 모임 참여/신청 |
| 모임 | PATCH | `/joins/{joinId}` | 모임 참여 승인/거절 |
| 신고 | POST | `/reports` | 사용자 신고 등록 |
| 제재 | GET | `/restrictions/{userId}` | 사용자 제재 상태 조회 |

## 5. 상세 명세
### 5.1 게시글 생성
- Method: `POST`
- Path: `/api/community/posts`
- 설명: `GATHERING` 또는 `FREE_FEED` 게시글을 생성한다.

#### Request Body
```json
{
  "authorUserId": 1,
  "cityCode": "SYD",
  "type": "GATHERING",
  "content": "블루마운틴 같이 가실 분",
  "imageUrl": null,
  "locationName": "Sydney",
  "destination": "Blue Mountain",
  "meetingPlace": "Town Hall",
  "meetingAt": "2026-03-27T10:00:00",
  "maxParticipants": 6,
  "joinPolicy": "APPROVAL"
}
```

#### Request Description
| Field | Type | Required | Description |
|---|---|---|---|
| authorUserId | Number | Y | 작성자 사용자 ID |
| cityCode | String | Y | 도시 코드(초기 `SYD`) |
| type | Enum | Y | `GATHERING` 또는 `FREE_FEED` |
| content | String | Y | 게시글 본문 |
| imageUrl | String | N | 이미지 URL |
| locationName | String | N | 위치 텍스트 |
| destination | String | 조건부 | `GATHERING` 필수 |
| meetingPlace | String | 조건부 | `GATHERING` 필수 |
| meetingAt | DateTime | 조건부 | `GATHERING` 필수 |
| maxParticipants | Number | 조건부 | `GATHERING` 필수 |
| joinPolicy | Enum | 조건부 | `GATHERING` 필수 (`APPROVAL`/`FREE`) |

#### Response Body (201)
```json
{
  "id": 10,
  "authorUserId": 1,
  "cityCode": "SYD",
  "type": "GATHERING",
  "content": "블루마운틴 같이 가실 분",
  "imageUrl": null,
  "locationName": "Sydney",
  "destination": "Blue Mountain",
  "meetingPlace": "Town Hall",
  "meetingAt": "2026-03-27T10:00:00",
  "maxParticipants": 6,
  "joinPolicy": "APPROVAL"
}
```

#### Error Response
| Code | Message | Description |
|---|---|---|
| INVALID_POST_PAYLOAD | Gathering post requires ... | 타입별 필드 정책 위반 |
| USER_RESTRICTED | User is restricted until ... | 제재 기간 중 작성 시도 |

### 5.2 게시글 조회
- Method: `GET`
- Path: `/api/community/posts?cityCode=SYD&type=ALL|GATHERING|FREE_FEED`
- 설명: 도시별 피드를 타입 필터로 조회한다.

#### Query Parameter
| Name | Type | Required | Description | Example |
|---|---|---|---|---|
| cityCode | String | Y | 도시 코드 | `SYD` |
| type | String | N | 필터 타입(기본 `ALL`) | `FREE_FEED` |

#### Response Body (200)
```json
[
  {
    "id": 10,
    "authorUserId": 1,
    "cityCode": "SYD",
    "type": "FREE_FEED",
    "content": "오페라하우스 뷰 최고",
    "imageUrl": "https://...",
    "locationName": "Opera House",
    "destination": null,
    "meetingPlace": null,
    "meetingAt": null,
    "maxParticipants": null,
    "joinPolicy": null
  }
]
```

### 5.3 모임 참여/신청
- Method: `POST`
- Path: `/api/community/posts/{postId}/join`
- 설명: 모임글에 참여한다. 승인형이면 `PENDING`, 자유참여형이면 `APPROVED`.

#### Request Body
```json
{
  "userId": 7
}
```

#### Response Body (201)
```json
{
  "joinId": 1,
  "postId": 10,
  "userId": 7,
  "status": "PENDING"
}
```

#### Error Response
| Code | Message | Description |
|---|---|---|
| POST_NOT_FOUND | Post not found | 게시글 없음 |
| JOIN_NOT_ALLOWED | Only gathering posts... | 자유형글 참여 시도/정원 초과 |
| JOIN_ALREADY_EXISTS | Join already exists | 중복 참여 |
| USER_RESTRICTED | User is restricted ... | 제재 사용자 참여 시도 |

### 5.4 모임 참여 승인/거절
- Method: `PATCH`
- Path: `/api/community/joins/{joinId}`
- 설명: 모임장이 승인형 모임의 참여 신청을 승인/거절한다.

#### Request Body
```json
{
  "hostUserId": 1,
  "status": "APPROVED"
}
```

#### Response Body (200)
```json
{
  "joinId": 1,
  "postId": 10,
  "userId": 7,
  "status": "APPROVED"
}
```

#### Error Response
| Code | Message | Description |
|---|---|---|
| JOIN_NOT_FOUND | Join not found | 참여 신청 없음 |
| FORBIDDEN_ACTION | Only host can review joins | 모임장 아님 |
| JOIN_NOT_ALLOWED | Review is allowed only... | 승인형 모임 아님/잘못된 상태 |

### 5.5 사용자 신고
- Method: `POST`
- Path: `/api/community/reports`
- 설명: 대상 사용자를 신고하고 누적 건수를 반환한다.

#### Request Body
```json
{
  "reporterUserId": 1,
  "targetUserId": 9,
  "reason": "폭언"
}
```

#### Response Body (201)
```json
{
  "targetUserId": 9,
  "reportCount": 3,
  "restricted": true
}
```

### 5.6 제재 상태 조회
- Method: `GET`
- Path: `/api/community/restrictions/{userId}`
- 설명: 사용자의 활성 제재 여부를 조회한다.

#### Response Body (200)
```json
{
  "active": true,
  "startAt": "2026-03-26T12:00:00",
  "endAt": "2026-04-02T12:00:00",
  "reason": "3 reports accumulated"
}
```

## 6. 비즈니스 규칙
- `FREE_FEED`는 모집 필드를 포함할 수 없다.
- `GATHERING`은 모집 필드(목적지/장소/시간/정원/참여방식)가 필수다.
- 신고 3회 이상 누적 시 `7일` 커뮤니티 활동 제한을 부여한다.

## 7. 이력
- v0.1: 게시글/참여/신고/제재 API 최초 작성
