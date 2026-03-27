package lab.coder.colly.domain.auth.adapter.in.web;

import jakarta.validation.Valid;
import lab.coder.colly.domain.auth.adapter.in.web.dto.RequestMagicLinkRequest;
import lab.coder.colly.domain.auth.adapter.in.web.dto.VerifyMagicLinkRequest;
import lab.coder.colly.domain.auth.application.port.in.RequestMagicLinkUseCase;
import lab.coder.colly.domain.auth.application.port.in.VerifyMagicLinkUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RequestMagicLinkUseCase requestMagicLinkUseCase;
    private final VerifyMagicLinkUseCase verifyMagicLinkUseCase;

    /**
     * 매직링크 발급을 요청한다.
     *
     * @param request 발급 요청 바디
     * @return 발급 결과 응답
     */
    @PostMapping("/magic-link/request")
    public ResponseEntity<RequestMagicLinkUseCase.MagicLinkRequestResult> request(
            @Valid @RequestBody RequestMagicLinkRequest request
    ) {
        RequestMagicLinkUseCase.MagicLinkRequestResult result = requestMagicLinkUseCase.request(
            new RequestMagicLinkUseCase.MagicLinkRequestCommand(request.email())
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * 매직링크 토큰을 검증하고 로그인 처리한다.
     *
     * @param request 검증 요청 바디
     * @return 로그인 결과 응답
     */
    @PostMapping("/magic-link/verify")
    public ResponseEntity<VerifyMagicLinkUseCase.LoginResult> verify(
            @Valid @RequestBody VerifyMagicLinkRequest request
    ) {
        VerifyMagicLinkUseCase.LoginResult result = verifyMagicLinkUseCase.verify(
            new VerifyMagicLinkUseCase.VerifyMagicLinkCommand(request.token())
        );

        return ResponseEntity.ok(result);
    }
}
