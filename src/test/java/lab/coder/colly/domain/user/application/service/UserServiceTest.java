package lab.coder.colly.domain.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import lab.coder.colly.domain.user.application.port.in.CreateUserUseCase;
import lab.coder.colly.domain.user.application.port.in.UserView;
import lab.coder.colly.domain.user.application.port.out.UserRepositoryPort;
import lab.coder.colly.domain.user.domain.model.User;
import lab.coder.colly.shared.error.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private UserService userService;

    @Test
    void create_savesUserAndReturnsView() {
        when(userRepositoryPort.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepositoryPort.save(any(User.class)))
            .thenReturn(User.restore(1L, "test@example.com", "tester"));

        UserView result = userService.create(new CreateUserUseCase.CreateUserCommand("test@example.com", "tester"));

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.email()).isEqualTo("test@example.com");
    }

    @Test
    void getById_throwsWhenNotFound() {
        when(userRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(99L))
            .isInstanceOf(DomainException.class);
    }
}
