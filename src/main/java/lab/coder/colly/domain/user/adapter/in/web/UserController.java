package lab.coder.colly.domain.user.adapter.in.web;

import jakarta.validation.Valid;
import lab.coder.colly.domain.user.adapter.in.web.dto.CreateUserRequest;
import lab.coder.colly.domain.user.adapter.in.web.dto.UserResponse;
import lab.coder.colly.domain.user.application.port.in.CreateUserUseCase;
import lab.coder.colly.domain.user.application.port.in.GetUserUseCase;
import lab.coder.colly.domain.user.application.port.in.UserView;
import lab.coder.colly.shared.api.ApiResponse;
import lab.coder.colly.shared.api.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;

    /**
     * 사용자 생성 API.
     *
     * @param request 사용자 생성 요청 바디
     * @return 생성된 사용자 응답
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(
            @Valid @RequestBody CreateUserRequest request
    ) {
        UserView view = createUserUseCase.create(
                new CreateUserUseCase.CreateUserCommand(
                        request.email(),
                        request.name()
                )
        );

        return ApiResponses.created(UserResponse.from(view));
    }

    /**
     * 사용자 단건 조회 API.
     *
     * @param id 사용자 식별자
     * @return 조회된 사용자 응답
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable Long id) {
        return ApiResponses.ok(UserResponse.from(getUserUseCase.getById(id)));
    }
}
