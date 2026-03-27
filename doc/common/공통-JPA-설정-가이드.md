# 공통 JPA/Querydsl 설정 가이드

## 1. 왜 필요한가
- 도메인별로 JPA/Auditing/Querydsl 설정을 중복 작성하지 않기 위함.
- 공통 베이스 엔티티와 유틸을 통해 일관된 퍼시스턴스 코딩 스타일을 유지하기 위함.

## 2. 현재 기준 구성 요소
| 클래스 | 경로 | 역할 |
|---|---|---|
| `JpaConfig` | `src/main/java/lab/coder/colly/shared/config/JpaConfig.java` | JPA Auditing 활성화 |
| `QuerydslConfig` | `src/main/java/lab/coder/colly/shared/config/QuerydslConfig.java` | `JPAQueryFactory` 빈 등록 |
| `BaseEntity` | `src/main/java/lab/coder/colly/shared/persistence/BaseEntity.java` | `createdAt`, `updatedAt` 공통 감사 컬럼 |
| `JpaSupport` | `src/main/java/lab/coder/colly/shared/persistence/JpaSupport.java` | JPA 조회 보조 유틸 |

## 3. 언제/어디서 사용하나
- `JpaConfig`
  - 언제: 감사 컬럼 자동 관리가 필요할 때
  - 어디서: 모든 JPA 엔티티 저장/수정 처리
- `QuerydslConfig`
  - 언제: 복잡 조회를 Querydsl로 구현할 때
  - 어디서: `adapter.out.persistence` 조회 어댑터/커스텀 리포지토리
- `BaseEntity`
  - 언제: 생성일/수정일이 필요한 엔티티 작성 시
  - 어디서: 각 도메인 엔티티 클래스의 상속 베이스
- `JpaSupport`
  - 언제: null 조회값을 즉시 예외로 변환할 때
  - 어디서: persistence adapter/repository 보조 로직

## 4. 어떻게 사용하나
### 4.1 BaseEntity 상속
```java
public class UserEntity extends BaseEntity {
    // createdAt, updatedAt 자동 관리
}
```

### 4.2 Querydsl 사용
```java
@RequiredArgsConstructor
public class SomeQueryRepository {
    private final JPAQueryFactory queryFactory;
}
```

### 4.3 JpaSupport 사용
```java
UserEntity entity = JpaSupport.getOrThrow(found, () -> "User not found: " + id);
```

## 5. 팀 규칙
- 감사 컬럼은 수동 세팅하지 않는다. (`BaseEntity` + Auditing 사용)
- 복잡 조회는 Querydsl을 기본으로 하고, 네이티브 SQL은 예외적으로만 사용한다.
- 공통 설정/유틸은 `shared`에 두고, 도메인 내부에 같은 역할 코드를 중복 생성하지 않는다.
