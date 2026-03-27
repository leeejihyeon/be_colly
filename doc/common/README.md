# 공통 모듈 문서

## 1. 목적
- `shared` 패키지에 있는 공통 기능의 역할/사용 방법을 팀 공통 기준으로 관리한다.
- 도메인마다 응답/에러/설정 구현이 분산되지 않도록 표준을 고정한다.

## 2. 문서 목록
- `공통-응답-가이드.md`
  - `ApiResponse`, `ApiErrorResponse`, `ApiResponses` 사용 규칙
- `공통-에러-처리-가이드.md`
  - `ErrorCode`, `DomainException`, `GlobalExceptionHandler` 규칙
- `공통-JPA-설정-가이드.md`
  - `JpaConfig`, `QuerydslConfig`, `BaseEntity`, `JpaSupport` 규칙

## 3. 적용 범위
- 코드 경로: `src/main/java/lab/coder/colly/shared/**`
- 모든 도메인(`auth`, `community`, `order`, `user`)에서 동일하게 적용한다.
