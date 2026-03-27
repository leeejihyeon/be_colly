package lab.coder.colly.domain.auth.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lab.coder.colly.shared.persistence.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "auth_magic_link")
public class AuthMagicLinkEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 320)
    private String email;

    @Column(nullable = false, unique = true, length = 64)
    private String tokenHash;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime usedAt;

    /**
     * 매직 링크 인증 토큰 저장용 엔티티를 생성한다.
     *
     * @param id 엔티티 식별자
     * @param email 인증 대상 이메일
     * @param tokenHash 매직 링크 원문 토큰의 해시값
     * @param expiresAt 토큰 만료 시각
     * @param usedAt 토큰 사용 시각
     */
    public AuthMagicLinkEntity(
            Long id,
            String email,
            String tokenHash,
            LocalDateTime expiresAt,
            LocalDateTime usedAt
    ) {
        this.id = id;
        this.email = email;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.usedAt = usedAt;
    }
}
