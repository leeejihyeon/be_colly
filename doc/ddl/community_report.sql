CREATE TABLE IF NOT EXISTS community_report (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  reporter_user_id BIGINT NOT NULL COMMENT '신고자 사용자 ID',
  target_user_id BIGINT NOT NULL COMMENT '피신고자 사용자 ID',
  reason VARCHAR(500) NOT NULL COMMENT '신고 사유',
  created_at DATETIME(6) NOT NULL COMMENT '생성 시각',
  updated_at DATETIME(6) NOT NULL COMMENT '수정 시각',
  KEY idx_community_report_target_user_id (target_user_id),
  KEY idx_community_report_reporter_user_id (reporter_user_id),
  CONSTRAINT fk_community_report_reporter_user
    FOREIGN KEY (reporter_user_id) REFERENCES users (id),
  CONSTRAINT fk_community_report_target_user
    FOREIGN KEY (target_user_id) REFERENCES users (id)
) COMMENT='커뮤니티 신고 이력';
