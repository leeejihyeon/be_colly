CREATE TABLE IF NOT EXISTS accommodations (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  country_code VARCHAR(2) NOT NULL COMMENT '국가 코드',
  city_code VARCHAR(50) NOT NULL COMMENT '도시 코드',
  name VARCHAR(200) NOT NULL COMMENT '숙소명',
  address_line1 VARCHAR(255) NULL COMMENT '주소 1',
  address_line2 VARCHAR(255) NULL COMMENT '주소 2',
  created_at DATETIME(6) NOT NULL COMMENT '생성 시각',
  updated_at DATETIME(6) NOT NULL COMMENT '수정 시각',
  KEY idx_accommodations_country_city (country_code, city_code),
  KEY idx_accommodations_name (name)
) COMMENT='숙소 마스터';
