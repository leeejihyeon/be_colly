CREATE TABLE IF NOT EXISTS user_restriction (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  user_id BIGINT NOT NULL COMMENT '제재 대상 사용자 ID',
  type VARCHAR(50) NOT NULL COMMENT '제재 유형(COMMUNITY_ACTIVITY_BAN)',
  start_at DATETIME(6) NOT NULL COMMENT '제재 시작 시각',
  end_at DATETIME(6) NOT NULL COMMENT '제재 종료 시각',
  reason VARCHAR(200) NOT NULL COMMENT '제재 사유',
  created_at DATETIME(6) NOT NULL COMMENT '생성 시각',
  updated_at DATETIME(6) NOT NULL COMMENT '수정 시각',
  KEY idx_user_restriction_user_time (user_id, start_at, end_at, id),
  CONSTRAINT fk_user_restriction_user
    FOREIGN KEY (user_id) REFERENCES users (id)
) COMMENT='사용자 제재 이력';
