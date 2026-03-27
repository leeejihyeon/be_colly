CREATE TABLE IF NOT EXISTS user_auth_identity (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  user_id BIGINT NOT NULL COMMENT '사용자 ID',
  provider VARCHAR(50) NOT NULL COMMENT '인증 제공자',
  provider_user_id VARCHAR(191) NOT NULL COMMENT '제공자 사용자 식별자',
  created_at DATETIME(6) NOT NULL COMMENT '생성 시각',
  updated_at DATETIME(6) NOT NULL COMMENT '수정 시각',
  CONSTRAINT uk_user_auth_identity_provider_user UNIQUE (provider, provider_user_id),
  KEY idx_user_auth_identity_user_id (user_id),
  CONSTRAINT fk_user_auth_identity_user
    FOREIGN KEY (user_id) REFERENCES users (id)
) COMMENT='사용자 인증 수단 연결';
