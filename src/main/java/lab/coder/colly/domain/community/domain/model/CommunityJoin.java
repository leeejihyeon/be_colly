package lab.coder.colly.domain.community.domain.model;

public class CommunityJoin {

    private final Long id;
    private final Long postId;
    private final Long userId;
    private final JoinStatus status;

    private CommunityJoin(Long id, Long postId, Long userId, JoinStatus status) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.status = status;
    }

    public static CommunityJoin create(Long postId, Long userId, JoinStatus status) {
        return new CommunityJoin(null, postId, userId, status);
    }

    public static CommunityJoin restore(Long id, Long postId, Long userId, JoinStatus status) {
        return new CommunityJoin(id, postId, userId, status);
    }

    public CommunityJoin changeStatus(JoinStatus nextStatus) {
        return new CommunityJoin(id, postId, userId, nextStatus);
    }

    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getUserId() {
        return userId;
    }

    public JoinStatus getStatus() {
        return status;
    }
}
