# 공통 API 명세서

## 1. 문서 정보
- 작성일: 2026-03-26
- 작성자: Codex
- 적용 대상: `colly` 전체 API

## 2. 공통 규칙
- Base Path:
  - 인증: `/api/auth/**`
  - 커뮤니티: `/api/community/**`
- Content-Type:
  - 요청/응답 기본값: `application/json`
- 시간 포맷:
  - 날짜시간은 ISO-8601 (`yyyy-MM-dd'T'HH:mm:ss`) 사용
- 인증 방식(현황):
  - `auth` API는 비인증 진입점
  - `community` API는 현재 요청 바디/파라미터의 `userId` 사용
  - 추후 토큰 인증으로 통합 예정

## 3. 공통 에러 응답 포맷
```json
{
  "errorCode": "ERROR_CODE",
  "message": "상세 메시지"
}
```

### 3.1 Validation 에러 포맷
```json
{
  "errorCode": "VALIDATION_ERROR",
  "message": "fieldName 에러메시지"
}
```

## 4. 공통 에러 코드 매핑
| HTTP Status | ErrorCode | 설명 |
|---|---|---|
| 400 | INVALID_AMOUNT | 금액 정책 위반 |
| 400 | INVALID_POST_PAYLOAD | 게시글 타입별 필드 정책 위반 |
| 400 | JOIN_NOT_ALLOWED | 허용되지 않은 참여/승인 요청 |
| 400 | AUTH_INVALID_EMAIL | 이메일 형식 오류 |
| 400 | MAGIC_LINK_EXPIRED | 만료된 매직링크 |
| 400 | MAGIC_LINK_ALREADY_USED | 이미 사용된 매직링크 |
| 400 | VALIDATION_ERROR | Bean Validation 실패 |
| 403 | USER_RESTRICTED | 제재 사용자의 제한된 동작 |
| 403 | FORBIDDEN_ACTION | 권한 없는 사용자 동작 |
| 404 | USER_NOT_FOUND | 사용자 없음 |
| 404 | ORDER_NOT_FOUND | 주문 없음 |
| 404 | POST_NOT_FOUND | 게시글 없음 |
| 404 | JOIN_NOT_FOUND | 참여 신청 없음 |
| 404 | MAGIC_LINK_NOT_FOUND | 매직링크 없음 |
| 409 | DUPLICATE_EMAIL | 중복 이메일 |
| 409 | JOIN_ALREADY_EXISTS | 중복 참여 신청 |
| 429 | MAGIC_LINK_RATE_LIMITED | 매직링크 재요청 과다 |

## 5. 공통 작성 규칙 (도메인 문서용)
- 각 도메인 문서에는 아래를 반드시 포함한다.
  - 엔드포인트 목록
  - Request Description (필드 타입/필수 여부/설명)
  - Response Description (핵심 필드 의미)
  - Error Response 코드와 발생 조건
- 구현 코드 변경 시 도메인 문서와 이 공통 문서를 함께 갱신한다.

## 6. 이력
- v0.1: 공통 템플릿 생성
- v0.2: auth/community 실제 구현 기준으로 공통 규칙 최신화
