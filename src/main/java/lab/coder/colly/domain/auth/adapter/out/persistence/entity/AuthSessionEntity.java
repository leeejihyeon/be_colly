package lab.coder.colly.domain.auth.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lab.coder.colly.domain.auth.domain.model.AuthProvider;
import lab.coder.colly.shared.persistence.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "auth_session")
public class AuthSessionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AuthProvider provider;

    @Column(nullable = false, length = 64)
    private String refreshTokenHash;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    /**
     * 로그인 세션 저장용 엔티티를 생성한다.
     *
     * @param id 엔티티 식별자
     * @param userId 세션 소유 사용자 ID
     * @param provider 로그인 인증 제공자
     * @param refreshTokenHash 리프레시 토큰 해시값
     * @param expiresAt 세션 만료 시각
     */
    public AuthSessionEntity(
            Long id,
            Long userId,
            AuthProvider provider,
            String refreshTokenHash,
            LocalDateTime expiresAt
    ) {
        this.id = id;
        this.userId = userId;
        this.provider = provider;
        this.refreshTokenHash = refreshTokenHash;
        this.expiresAt = expiresAt;
    }
}
