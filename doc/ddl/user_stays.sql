CREATE TABLE IF NOT EXISTS user_stays (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  user_id BIGINT NOT NULL COMMENT '사용자 ID',
  accommodation_id BIGINT NOT NULL COMMENT '숙소 PK',
  check_in DATE NOT NULL COMMENT '체크인 일자',
  check_out DATE NOT NULL COMMENT '체크아웃 일자',
  room_type VARCHAR(120) NULL COMMENT '객실 타입',
  created_at DATETIME(6) NOT NULL COMMENT '생성 시각',
  updated_at DATETIME(6) NOT NULL COMMENT '수정 시각',
  KEY idx_user_stays_user_id (user_id),
  KEY idx_user_stays_accommodation_id (accommodation_id),
  KEY idx_user_stays_date_range (check_in, check_out),
  CONSTRAINT fk_user_stays_user
    FOREIGN KEY (user_id) REFERENCES users (id),
  CONSTRAINT fk_user_stays_accommodation
    FOREIGN KEY (accommodation_id) REFERENCES accommodations (id),
  CONSTRAINT chk_user_stays_date_range
    CHECK (check_in <= check_out)
) COMMENT='사용자 숙박 이력';
