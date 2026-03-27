# TODO 작업 목록

## 1. 목적
- 코드/문서 전반의 미완료 항목(TODO)을 한 곳에서 추적하고, 배포 전 일괄 점검한다.

## 2. 진행 항목
| 구분 | 항목 | 위치 | 완료 조건 |
|---|---|---|---|
| 완료 | 매직링크 로그인 API 구현 | `src/main/java/lab/coder/colly/domain/auth` | 완료 |
| 완료 | 커뮤니티 게시글 타입 정책 구현 (`GATHERING/FREE_FEED`) | `src/main/java/lab/coder/colly/domain/community` | 완료 |
| 완료 | 신고 3회 누적 7일 제한 로직 구현 | `src/main/java/lab/coder/colly/domain/community` | 완료 |
| 완료 | 인증/커뮤니티 API 명세서 최신화 | `doc/api/auth`, `doc/api/community` | 완료 |
| 완료 | 인증/커뮤니티 DDL 문서 최신화 | `doc/ddl/*.sql` | 완료 |
| 다음 | 실제 메일 발송 연동 (magicToken 외부 발송) | `domain/auth/application/service/AuthService.java` | SMTP/메일서비스 연동 후 토큰 응답 직접 노출 제거 |
| 다음 | 커뮤니티 API 인증 방식 개선 | `domain/community/adapter/in/web/CommunityController.java` | 사용자 ID 파라미터 제거, 토큰 기반 사용자 식별 적용 |
| 다음 | 숙소/체류 겹침 기능 구현 | `domain/accommodation` | 숙소 등록 및 overlap 조회 API 완료 |

## 3. 운영 규칙
- TODO를 새로 추가/제거할 때 본 파일을 함께 갱신한다.
- 배포 전 본 파일의 미완료 항목을 점검한다.
