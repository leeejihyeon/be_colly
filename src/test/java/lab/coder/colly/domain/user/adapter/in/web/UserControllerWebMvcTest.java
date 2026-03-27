package lab.coder.colly.domain.user.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lab.coder.colly.domain.user.application.port.in.CreateUserUseCase;
import lab.coder.colly.domain.user.application.port.in.GetUserUseCase;
import lab.coder.colly.domain.user.application.port.in.UserView;
import lab.coder.colly.shared.error.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateUserUseCase createUserUseCase;

    @MockBean
    private GetUserUseCase getUserUseCase;

    @Test
    void create_returns201() throws Exception {
        when(createUserUseCase.create(any()))
            .thenReturn(new UserView(1L, "api@example.com", "api-user"));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "email": "api@example.com",
                      "name": "api-user"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L));
    }
}
