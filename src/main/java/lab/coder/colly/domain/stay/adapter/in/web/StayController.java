package lab.coder.colly.domain.stay.adapter.in.web;

import jakarta.validation.Valid;
import java.util.List;
import lab.coder.colly.domain.stay.adapter.in.web.dto.UpsertUserStayRequest;
import lab.coder.colly.domain.stay.application.port.in.CreateUserStayUseCase;
import lab.coder.colly.domain.stay.application.port.in.DeleteUserStayUseCase;
import lab.coder.colly.domain.stay.application.port.in.ListMyUserStaysUseCase;
import lab.coder.colly.domain.stay.application.port.in.ListStayOverlapsUseCase;
import lab.coder.colly.domain.stay.application.port.in.StayOverlapView;
import lab.coder.colly.domain.stay.application.port.in.UpdateUserStayUseCase;
import lab.coder.colly.domain.stay.application.port.in.UserStayView;
import lab.coder.colly.shared.api.ApiResponse;
import lab.coder.colly.shared.api.ApiResponses;
import lab.coder.colly.shared.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stays")
@RequiredArgsConstructor
public class StayController {

    private final CreateUserStayUseCase createUserStayUseCase;
    private final ListMyUserStaysUseCase listMyUserStaysUseCase;
    private final UpdateUserStayUseCase updateUserStayUseCase;
    private final DeleteUserStayUseCase deleteUserStayUseCase;
    private final ListStayOverlapsUseCase listStayOverlapsUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<UserStayView>> create(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody UpsertUserStayRequest request
    ) {
        return ApiResponses.created(
                createUserStayUseCase.create(
                        new CreateUserStayUseCase.CreateUserStayCommand(
                                authenticatedUser.userId(),
                                request.accommodationId(),
                                request.checkIn(),
                                request.checkOut()
                        )
                )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<UserStayView>>> listMyStays(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        return ApiResponses.ok(listMyUserStaysUseCase.listMyStays(authenticatedUser.userId()));
    }

    @PutMapping("/{stayId}")
    public ResponseEntity<ApiResponse<UserStayView>> update(
            @PathVariable Long stayId,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody UpsertUserStayRequest request
    ) {
        return ApiResponses.ok(
                updateUserStayUseCase.update(
                        new UpdateUserStayUseCase.UpdateUserStayCommand(
                                stayId,
                                authenticatedUser.userId(),
                                request.accommodationId(),
                                request.checkIn(),
                                request.checkOut()
                        )
                )
        );
    }

    @DeleteMapping("/{stayId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long stayId,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        deleteUserStayUseCase.delete(
                new DeleteUserStayUseCase.DeleteUserStayCommand(stayId, authenticatedUser.userId())
        );
        return ApiResponses.ok(null);
    }

    @GetMapping("/{stayId}/overlaps")
    public ResponseEntity<ApiResponse<List<StayOverlapView>>> listOverlaps(
            @PathVariable Long stayId,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        return ApiResponses.ok(
                listStayOverlapsUseCase.listOverlaps(
                        new ListStayOverlapsUseCase.ListStayOverlapsQuery(stayId, authenticatedUser.userId())
                )
        );
    }
}
