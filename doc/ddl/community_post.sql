CREATE TABLE IF NOT EXISTS community_post (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  author_user_id BIGINT NOT NULL COMMENT '작성자 사용자 ID',
  city_code VARCHAR(50) NOT NULL COMMENT '도시 코드',
  type VARCHAR(20) NOT NULL COMMENT '게시글 타입(FREE_FEED/GATHERING)',
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
  KEY idx_community_post_city_code_id (city_code, id),
  KEY idx_community_post_city_code_type_id (city_code, type, id),
  CONSTRAINT fk_community_post_author_user
    FOREIGN KEY (author_user_id) REFERENCES users (id)
) COMMENT='커뮤니티 게시글';
