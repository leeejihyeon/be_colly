package lab.coder.colly.domain.stay.adapter.in.web;

import java.util.List;
import lab.coder.colly.domain.stay.application.port.in.AccommodationView;
import lab.coder.colly.domain.stay.application.port.in.ListAccommodationsUseCase;
import lab.coder.colly.shared.api.ApiResponse;
import lab.coder.colly.shared.api.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

    private final ListAccommodationsUseCase listAccommodationsUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccommodationView>>> list(
            @RequestParam String countryCode,
            @RequestParam String cityCode
    ) {
        return ApiResponses.ok(
                listAccommodationsUseCase.list(
                        new ListAccommodationsUseCase.ListAccommodationsQuery(countryCode, cityCode)
                )
        );
    }
}
