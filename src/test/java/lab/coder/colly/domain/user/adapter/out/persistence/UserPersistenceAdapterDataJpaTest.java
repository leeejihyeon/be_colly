package lab.coder.colly.domain.user.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import lab.coder.colly.domain.user.adapter.out.persistence.mapper.UserPersistenceMapper;
import lab.coder.colly.domain.user.adapter.out.persistence.repository.UserJpaRepository;
import lab.coder.colly.domain.user.domain.model.User;
import lab.coder.colly.shared.config.JpaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({JpaConfig.class, UserPersistenceAdapter.class, UserPersistenceMapper.class})
class UserPersistenceAdapterDataJpaTest {

    @Autowired
    private UserPersistenceAdapter userPersistenceAdapter;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    void save_and_findById_workThroughMapper() {
        User saved = userPersistenceAdapter.save(User.create("jpa@example.com", "jpa-user"));

        assertThat(saved.getId()).isNotNull();
        assertThat(userPersistenceAdapter.findById(saved.getId())).isPresent();
        assertThat(userJpaRepository.existsByEmail("jpa@example.com")).isTrue();
    }
}
