package lab.coder.colly.domain.user.adapter.out.persistence.mapper;

import lab.coder.colly.domain.user.adapter.out.persistence.entity.UserEntity;
import lab.coder.colly.domain.user.domain.model.User;
import org.springframework.stereotype.Component;

/**
 * 사용자 도메인 모델과 JPA 엔티티 간 변환 매퍼.
 */
@Component
public class UserPersistenceMapper {

    /**
     * 사용자 도메인 모델을 사용자 엔티티로 변환한다.
     *
     * @param user 사용자 도메인 모델
     * @return 사용자 엔티티
     */
    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    /**
     * 사용자 엔티티를 사용자 도메인 모델로 변환한다.
     *
     * @param entity 사용자 엔티티
     * @return 사용자 도메인 모델
     */
    public User toDomain(UserEntity entity) {
        return User.restore(
                entity.getId(),
                entity.getEmail(),
                entity.getName()
        );
    }
}
