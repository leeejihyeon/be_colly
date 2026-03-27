package lab.coder.colly.domain.user.adapter.out.persistence.mapper;

import lab.coder.colly.domain.user.adapter.out.persistence.entity.UserEntity;
import lab.coder.colly.domain.user.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public UserEntity toEntity(User user) {
        return new UserEntity(user.getId(), user.getEmail(), user.getName());
    }

    public User toDomain(UserEntity entity) {
        return User.restore(entity.getId(), entity.getEmail(), entity.getName());
    }
}
