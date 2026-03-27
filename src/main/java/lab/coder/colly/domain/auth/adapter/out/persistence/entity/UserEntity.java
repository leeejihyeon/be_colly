package lab.coder.colly.domain.auth.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lab.coder.colly.shared.persistence.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
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
     * 인증 도메인에서 조회하는 사용자 엔티티를 생성한다.
     *
     * @param id 엔티티 식별자
     * @param email 사용자 이메일
     * @param name 사용자 이름
     */
    public UserEntity(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
