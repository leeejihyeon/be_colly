CREATE TABLE IF NOT EXISTS community_join (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  post_id BIGINT NOT NULL COMMENT '모임 게시글 ID',
  user_id BIGINT NOT NULL COMMENT '참여 사용자 ID',
  status VARCHAR(20) NOT NULL COMMENT '참여 상태(APPROVED/CANCELED/PENDING/REJECTED)',
  created_at DATETIME(6) NOT NULL COMMENT '생성 시각',
  updated_at DATETIME(6) NOT NULL COMMENT '수정 시각',
  UNIQUE KEY uk_community_join_post_user (post_id, user_id),
  KEY idx_community_join_user_id (user_id),
  KEY idx_community_join_post_id_status (post_id, status),
  CONSTRAINT fk_community_join_post
    FOREIGN KEY (post_id) REFERENCES community_post (id),
  CONSTRAINT fk_community_join_user
    FOREIGN KEY (user_id) REFERENCES users (id)
) COMMENT='모임 참여 신청/상태';
