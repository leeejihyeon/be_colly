package lab.coder.colly.domain.user.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lab.coder.colly.shared.persistence.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "userUserEntity")
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 320)
    private String email;

    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 사용자 도메인 저장용 엔티티를 생성한다.
     *
     * @param id    엔티티 식별자
     * @param email 사용자 이메일
     * @param name  사용자 이름
     */
    public UserEntity(
            Long id,
            String email,
            String name
    ) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
