-- =========================================================
-- Colly DB Schema (Annotated)
-- 기준: 사용자 제공 원본 DDL
-- =========================================================

-- 매직링크 로그인 요청/검증 토큰 저장 테이블
create table colly.auth_magic_link
(
    -- PK
    id         bigint auto_increment
        primary key,
    -- 생성 시각 (Auditing)
    created_at datetime(6)  not null,
    -- 수정 시각 (Auditing)
    updated_at datetime(6)  not null,
    -- 로그인 요청 이메일
    email      varchar(320) not null,
    -- 매직링크 만료 시각
    expires_at datetime(6)  not null,
    -- 원본 토큰의 해시값 (유니크)
    token_hash varchar(64)  not null,
    -- 토큰 사용 시각 (미사용이면 null)
    used_at    datetime(6)  null,
    -- 토큰 해시 유니크 제약
    constraint UK9hlrmm0kp4l3twtqbw2gswwqr
        unique (token_hash)
);

-- 로그인 세션(리프레시 토큰) 저장 테이블
create table colly.auth_session
(
    -- PK
    id                 bigint auto_increment
        primary key,
    -- 생성 시각 (Auditing)
    created_at         datetime(6) not null,
    -- 수정 시각 (Auditing)
    updated_at         datetime(6) not null,
    -- 세션 만료 시각
    expires_at         datetime(6) not null,
    -- 리프레시 토큰 해시
    refresh_token_hash varchar(64) not null,
    -- 세션 소유 사용자 ID
    user_id            bigint      not null
);

-- 사용자별 인증 수단(매직링크/소셜) 연결 테이블
create table colly.user_auth_identity
(
    -- PK
    id               bigint auto_increment
        primary key,
    -- 생성 시각 (Auditing)
    created_at       datetime(6)  not null,
    -- 수정 시각 (Auditing)
    updated_at       datetime(6)  not null,
    -- 사용자 ID
    user_id          bigint       not null,
    -- 인증 제공자 (GOOGLE/APPLE/FACEBOOK/EMAIL_MAGIC_LINK)
    provider         varchar(50)  not null,
    -- 제공자 내 사용자 식별자
    provider_user_id varchar(191) not null,
    -- 제공자+제공자 사용자 식별자 유니크
    constraint uk_user_auth_identity_provider_user
        unique (provider, provider_user_id)
);

-- 모임 참여 신청/상태 테이블
create table colly.community_join
(
    -- PK
    id         bigint auto_increment
        primary key,
    -- 생성 시각 (Auditing)
    created_at datetime(6)                                          not null,
    -- 수정 시각 (Auditing)
    updated_at datetime(6)                                          not null,
    -- 대상 게시글 ID
    post_id    bigint                                               not null,
    -- 참여 상태
    status     varchar(20)                    not null,
    -- 신청 사용자 ID
    user_id    bigint                                               not null
);

-- 커뮤니티 게시글 테이블 (모임글/자유형글)
create table colly.community_post
(
    -- PK
    id               bigint auto_increment
        primary key,
    -- 생성 시각 (Auditing)
    created_at       datetime(6)                     not null,
    -- 수정 시각 (Auditing)
    updated_at       datetime(6)                     not null,
    -- 작성자 사용자 ID
    author_user_id   bigint                          not null,
    -- 도시 코드 (예: SYD)
    city_code        varchar(50)                     not null,
    -- 게시글 본문
    content          varchar(4000)                   not null,
    -- 모임 목적지
    destination      varchar(200)                    null,
    -- 첨부 이미지 URL
    image_url        varchar(1000)                   null,
    -- 참여 정책 (승인형/자유참여형)
    join_policy      varchar(20)                    null,
    -- 자유형글 위치명
    location_name    varchar(200)                    null,
    -- 최대 참여 인원
    max_participants int                             null,
    -- 모임 시각
    meeting_at       datetime(6)                     null,
    -- 모임 장소
    meeting_place    varchar(200)                    null,
    -- 게시글 타입
    type             varchar(20)                    not null
);

-- 사용자 신고 이력 테이블
create table colly.community_report
(
    -- PK
    id               bigint auto_increment
        primary key,
    -- 생성 시각 (Auditing)
    created_at       datetime(6)  not null,
    -- 수정 시각 (Auditing)
    updated_at       datetime(6)  not null,
    -- 신고 사유
    reason           varchar(500) not null,
    -- 신고자 사용자 ID
    reporter_user_id bigint       not null,
    -- 신고 대상 사용자 ID
    target_user_id   bigint       not null
);

-- 주문/결제 정보 테이블
create table colly.orders
(
    -- PK
    id         bigint auto_increment
        primary key,
    -- 생성 시각 (Auditing)
    created_at datetime(6)    not null,
    -- 수정 시각 (Auditing)
    updated_at datetime(6)    not null,
    -- 결제 금액
    amount     decimal(19, 2) not null,
    -- 주문 사용자 ID
    user_id    bigint         not null
);

-- 사용자 제재 이력 테이블
create table colly.user_restriction
(
    -- PK
    id         bigint auto_increment
        primary key,
    -- 생성 시각 (Auditing)
    created_at datetime(6)                     not null,
    -- 수정 시각 (Auditing)
    updated_at datetime(6)                     not null,
    -- 제재 종료 시각
    end_at     datetime(6)                     not null,
    -- 제재 사유
    reason     varchar(200)                    not null,
    -- 제재 시작 시각
    start_at   datetime(6)                     not null,
    -- 제재 유형
    type       varchar(50)                    not null,
    -- 제재 대상 사용자 ID
    user_id    bigint                          not null
);

-- 사용자 기본 정보 테이블
create table colly.users
(
    -- PK
    id         bigint auto_increment
        primary key,
    -- 로그인/식별 이메일 (유니크)
    email      varchar(320) not null,
    -- 표시 이름
    name       varchar(100) not null,
    -- 생성 시각 (Auditing)
    created_at datetime(6)  not null,
    -- 수정 시각 (Auditing)
    updated_at datetime(6)  not null,
    -- 이메일 유니크 제약
    constraint UK6dotkott2kjsp8vw4d0m25fb7
        unique (email)
);
