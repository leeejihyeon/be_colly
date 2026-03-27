package lab.coder.colly.domain.auth.adapter.in.web;

import jakarta.validation.Valid;
import lab.coder.colly.domain.auth.adapter.in.web.dto.RequestMagicLinkRequest;
import lab.coder.colly.domain.auth.adapter.in.web.dto.SocialLoginRequest;
import lab.coder.colly.domain.auth.adapter.in.web.dto.VerifyMagicLinkRequest;
import lab.coder.colly.domain.auth.application.port.in.IssueMagicLinkUseCase;
import lab.coder.colly.domain.auth.application.port.in.SocialLoginUseCase;
import lab.coder.colly.domain.auth.application.port.in.VerifyMagicLinkUseCase;
import lab.coder.colly.domain.auth.domain.model.AuthProvider;
import lab.coder.colly.shared.api.ApiResponse;
import lab.coder.colly.shared.api.ApiResponses;
import lab.coder.colly.shared.error.DomainException;
import lab.coder.colly.shared.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IssueMagicLinkUseCase issueMagicLinkUseCase;
    private final VerifyMagicLinkUseCase verifyMagicLinkUseCase;
    private final SocialLoginUseCase socialLoginUseCase;

    /**
     * 매직링크 발급을 요청한다.
     *
     * @param request 발급 요청 바디
     * @return 발급 결과 응답
     */
    @PostMapping("/magic-link/request")
    public ResponseEntity<ApiResponse<IssueMagicLinkUseCase.MagicLinkIssueResult>> issueMagicLink(
            @Valid @RequestBody RequestMagicLinkRequest request
    ) {
        IssueMagicLinkUseCase.MagicLinkIssueResult result =
                issueMagicLinkUseCase.issue(
                        new IssueMagicLinkUseCase.MagicLinkIssueCommand(request.email())
                );

        return ApiResponses.created(result);
    }

    /**
     * 매직링크 토큰을 검증하고 로그인 처리한다.
     *
     * @param request 검증 요청 바디
     * @return 로그인 결과 응답
     */
    @PostMapping("/magic-link/verify")
    public ResponseEntity<ApiResponse<VerifyMagicLinkUseCase.LoginResult>> verify(
            @Valid @RequestBody VerifyMagicLinkRequest request
    ) {
        VerifyMagicLinkUseCase.LoginResult result =
                verifyMagicLinkUseCase.verify(
                        new VerifyMagicLinkUseCase.VerifyMagicLinkCommand(request.token())
                );

        return ApiResponses.ok(result);
    }

    /**
     * 소셜 계정으로 로그인 또는 회원가입을 처리한다.
     *
     * @param request 소셜 로그인 요청 바디
     * @return 로그인 결과 응답
     */
    @PostMapping("/social/login")
    public ResponseEntity<ApiResponse<SocialLoginUseCase.SocialLoginResult>> socialLogin(
            @Valid @RequestBody SocialLoginRequest request
    ) {
        AuthProvider provider = parseProvider(request.provider());

        SocialLoginUseCase.SocialLoginResult result =
                socialLoginUseCase.login(
                        new SocialLoginUseCase.SocialLoginCommand(
                                provider,
                                request.providerUserId(),
                                request.email(),
                                request.name()
                        )
                );

        return ApiResponses.ok(result);
    }

    /**
     * 소셜 제공자 문자열을 enum으로 변환한다.
     *
     * @param provider 제공자 문자열
     * @return 소셜 제공자 enum
     */
    private AuthProvider parseProvider(
            String provider
    ) {
        try {

            return AuthProvider.valueOf(provider.trim().toUpperCase());

        } catch (IllegalArgumentException ex) {

            throw new DomainException(
                    ErrorCode.AUTH_INVALID_PROVIDER,
                    "Unsupported provider: " + provider
            );
        }
    }
}
