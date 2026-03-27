package lab.coder.colly.domain.community.adapter.out.persistence.repository;

import java.util.List;
import lab.coder.colly.domain.community.adapter.out.persistence.entity.CommunityPostEntity;
import lab.coder.colly.domain.community.domain.model.PostType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityPostJpaRepository extends JpaRepository<CommunityPostEntity, Long> {

    List<CommunityPostEntity> findByCityCodeOrderByIdDesc(String cityCode);

    List<CommunityPostEntity> findByCityCodeAndTypeOrderByIdDesc(String cityCode, PostType type);
}
