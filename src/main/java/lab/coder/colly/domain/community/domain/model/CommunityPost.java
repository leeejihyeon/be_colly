package lab.coder.colly.domain.community.domain.model;

import java.time.LocalDateTime;

public class CommunityPost {

    private final Long id;
    private final Long authorUserId;
    private final String cityCode;
    private final PostType type;
    private final String content;
    private final String imageUrl;
    private final String locationName;
    private final String destination;
    private final String meetingPlace;
    private final LocalDateTime meetingAt;
    private final Integer maxParticipants;
    private final JoinPolicy joinPolicy;

    private CommunityPost(
        Long id,
        Long authorUserId,
        String cityCode,
        PostType type,
        String content,
        String imageUrl,
        String locationName,
        String destination,
        String meetingPlace,
        LocalDateTime meetingAt,
        Integer maxParticipants,
        JoinPolicy joinPolicy
    ) {
        this.id = id;
        this.authorUserId = authorUserId;
        this.cityCode = cityCode;
        this.type = type;
        this.content = content;
        this.imageUrl = imageUrl;
        this.locationName = locationName;
        this.destination = destination;
        this.meetingPlace = meetingPlace;
        this.meetingAt = meetingAt;
        this.maxParticipants = maxParticipants;
        this.joinPolicy = joinPolicy;
    }

    public static CommunityPost create(
        Long authorUserId,
        String cityCode,
        PostType type,
        String content,
        String imageUrl,
        String locationName,
        String destination,
        String meetingPlace,
        LocalDateTime meetingAt,
        Integer maxParticipants,
        JoinPolicy joinPolicy
    ) {
        return new CommunityPost(
            null,
            authorUserId,
            cityCode,
            type,
            content,
            imageUrl,
            locationName,
            destination,
            meetingPlace,
            meetingAt,
            maxParticipants,
            joinPolicy
        );
    }

    public static CommunityPost restore(
        Long id,
        Long authorUserId,
        String cityCode,
        PostType type,
        String content,
        String imageUrl,
        String locationName,
        String destination,
        String meetingPlace,
        LocalDateTime meetingAt,
        Integer maxParticipants,
        JoinPolicy joinPolicy
    ) {
        return new CommunityPost(
            id,
            authorUserId,
            cityCode,
            type,
            content,
            imageUrl,
            locationName,
            destination,
            meetingPlace,
            meetingAt,
            maxParticipants,
            joinPolicy
        );
    }

    public Long getId() {
        return id;
    }

    public Long getAuthorUserId() {
        return authorUserId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public PostType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getDestination() {
        return destination;
    }

    public String getMeetingPlace() {
        return meetingPlace;
    }

    public LocalDateTime getMeetingAt() {
        return meetingAt;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public JoinPolicy getJoinPolicy() {
        return joinPolicy;
    }
}
