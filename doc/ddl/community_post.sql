CREATE TABLE IF NOT EXISTS community_post (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  author_user_id BIGINT NOT NULL COMMENT '작성자 사용자 ID',
  country_code VARCHAR(2) NOT NULL COMMENT '국가 코드',
  city_code VARCHAR(50) NOT NULL COMMENT '도시 코드',
  type VARCHAR(20) NOT NULL COMMENT '게시글 타입(FREE_FEED/GATHERING)',
  audience_scope VARCHAR(30) NULL COMMENT '모임글 대상 범위(ACCOMMODATION_ONLY/CITY_WIDE)',
  accommodation_id BIGINT NULL COMMENT '숙소 PK(대상 범위가 숙소일 때)',
  audience_stay_start_date DATE NULL COMMENT '대상 숙박 시작일',
  audience_stay_end_date DATE NULL COMMENT '대상 숙박 종료일',
  content VARCHAR(4000) NOT NULL COMMENT '게시글 본문',
  image_url VARCHAR(1000) NULL COMMENT '이미지 URL',
  location_name VARCHAR(200) NULL COMMENT '자유형글 위치명',
  destination VARCHAR(200) NULL COMMENT '모임 목적지',
  meeting_place VARCHAR(200) NULL COMMENT '모임 장소',
  meeting_at DATETIME(6) NULL COMMENT '모임 시각',
  max_participants INT NULL COMMENT '최대 참여 인원',
  join_policy VARCHAR(20) NULL COMMENT '모임 참여 정책(APPROVAL/FREE)',
  created_at DATETIME(6) NOT NULL COMMENT '생성 시각',
  updated_at DATETIME(6) NOT NULL COMMENT '수정 시각',
  KEY idx_community_post_author_user_id (author_user_id),
  KEY idx_community_post_country_city_id (country_code, city_code, id),
  KEY idx_community_post_country_city_type_id (country_code, city_code, type, id),
  KEY idx_community_post_accommodation_id (accommodation_id),
  CONSTRAINT fk_community_post_author_user
    FOREIGN KEY (author_user_id) REFERENCES users (id),
  CONSTRAINT fk_community_post_accommodation
    FOREIGN KEY (accommodation_id) REFERENCES accommodations (id),
  CONSTRAINT chk_community_post_max_participants
    CHECK (max_participants IS NULL OR max_participants > 0),
  CONSTRAINT chk_community_post_stay_date_order
    CHECK (
      audience_stay_start_date IS NULL
      OR audience_stay_end_date IS NULL
      OR audience_stay_start_date <= audience_stay_end_date
    ),
  CONSTRAINT chk_community_post_free_feed_fields
    CHECK (
      type <> 'FREE_FEED'
      OR (
        audience_scope IS NULL
        AND accommodation_id IS NULL
        AND audience_stay_start_date IS NULL
        AND audience_stay_end_date IS NULL
        AND destination IS NULL
        AND meeting_place IS NULL
        AND meeting_at IS NULL
        AND max_participants IS NULL
        AND join_policy IS NULL
      )
    ),
  CONSTRAINT chk_community_post_gathering_required
    CHECK (
      type <> 'GATHERING'
      OR (
        audience_scope IS NOT NULL
        AND destination IS NOT NULL
        AND meeting_place IS NOT NULL
        AND meeting_at IS NOT NULL
        AND max_participants IS NOT NULL
        AND join_policy IS NOT NULL
      )
    ),
  CONSTRAINT chk_community_post_gathering_accommodation_scope
    CHECK (
      NOT (type = 'GATHERING' AND audience_scope = 'ACCOMMODATION_ONLY')
      OR (
        accommodation_id IS NOT NULL
        AND audience_stay_start_date IS NOT NULL
        AND audience_stay_end_date IS NOT NULL
      )
    ),
  CONSTRAINT chk_community_post_gathering_city_scope
    CHECK (
      NOT (type = 'GATHERING' AND audience_scope = 'CITY_WIDE')
      OR (
        accommodation_id IS NULL
        AND audience_stay_start_date IS NULL
        AND audience_stay_end_date IS NULL
      )
    )
) COMMENT='커뮤니티 게시글';
