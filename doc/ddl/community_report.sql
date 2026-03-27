CREATE TABLE IF NOT EXISTS community_report (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  reporter_user_id BIGINT NOT NULL,
  target_user_id BIGINT NOT NULL,
  reason VARCHAR(500) NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL
);
