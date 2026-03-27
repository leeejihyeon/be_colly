CREATE TABLE IF NOT EXISTS community_post (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  author_user_id BIGINT NOT NULL,
  city_code VARCHAR(50) NOT NULL,
  type VARCHAR(20) NOT NULL,
  content VARCHAR(4000) NOT NULL,
  image_url VARCHAR(1000) NULL,
  location_name VARCHAR(200) NULL,
  destination VARCHAR(200) NULL,
  meeting_place VARCHAR(200) NULL,
  meeting_at DATETIME NULL,
  max_participants INT NULL,
  join_policy VARCHAR(20) NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL
);
