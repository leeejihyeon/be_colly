package lab.coder.colly.domain.auth.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lab.coder.colly.domain.auth.domain.model.AuthProvider;
import lab.coder.colly.shared.persistence.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_auth_identity")
public class AuthIdentityEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AuthProvider provider;

    @Column(nullable = false, length = 191)
    private String providerUserId;

    /**
     * 사용자 인증 수단 식별자 엔티티를 생성한다.
     *
     * @param id             엔티티 식별자
     * @param userId         사용자 식별자
     * @param provider       인증 제공자
     * @param providerUserId 제공자 내 사용자 식별자
     */
    public AuthIdentityEntity(
            Long id,
            Long userId,
            AuthProvider provider,
            String providerUserId
    ) {
        this.id = id;
        this.userId = userId;
        this.provider = provider;
        this.providerUserId = providerUserId;
    }
}
