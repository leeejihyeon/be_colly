package lab.coder.colly.domain.stay.application.port.in;

import java.util.List;

public interface ListStayOverlapsUseCase {

    List<StayOverlapView> listOverlaps(ListStayOverlapsQuery query);

    record ListStayOverlapsQuery(
            Long stayId,
            Long userId
    ) {
    }
}
