CREATE TABLE IF NOT EXISTS auth_session (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  user_id BIGINT NOT NULL COMMENT '세션 소유 사용자 ID',
  refresh_token_hash VARCHAR(64) NOT NULL COMMENT '리프레시 토큰 해시',
  expires_at DATETIME(6) NOT NULL COMMENT '세션 만료 시각',
  created_at DATETIME(6) NOT NULL COMMENT '생성 시각',
  updated_at DATETIME(6) NOT NULL COMMENT '수정 시각',
  KEY idx_auth_session_user_id (user_id),
  CONSTRAINT fk_auth_session_user
    FOREIGN KEY (user_id) REFERENCES users (id)
) COMMENT='인증 세션 저장';
