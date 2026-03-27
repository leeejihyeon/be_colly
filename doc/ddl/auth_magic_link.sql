CREATE TABLE IF NOT EXISTS auth_magic_link (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  email VARCHAR(320) NOT NULL COMMENT '로그인 요청 이메일',
  token_hash VARCHAR(64) NOT NULL UNIQUE COMMENT '매직링크 토큰 해시(유니크)',
  expires_at DATETIME(6) NOT NULL COMMENT '토큰 만료 시각',
  used_at DATETIME(6) NULL COMMENT '토큰 사용 시각',
  created_at DATETIME(6) NOT NULL COMMENT '생성 시각',
  updated_at DATETIME(6) NOT NULL COMMENT '수정 시각',
  KEY idx_auth_magic_link_email_created_at (email, created_at)
) COMMENT='매직링크 인증 토큰 저장';
