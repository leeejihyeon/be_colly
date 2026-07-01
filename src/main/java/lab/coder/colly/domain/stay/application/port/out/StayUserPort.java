package lab.coder.colly.domain.stay.application.port.out;

import java.util.Map;
import java.util.Set;

public interface StayUserPort {

    Map<Long, String> findNamesByIds(Set<Long> userIds);
}
