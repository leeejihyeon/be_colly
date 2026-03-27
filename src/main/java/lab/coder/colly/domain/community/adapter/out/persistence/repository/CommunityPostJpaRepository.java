package lab.coder.colly.domain.community.adapter.out.persistence.repository;

import lab.coder.colly.domain.community.adapter.out.persistence.entity.CommunityPostEntity;
import lab.coder.colly.domain.community.domain.model.PostType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 커뮤니티 게시글 엔티티 조회용 JPA 리포지토리.
 */
public interface CommunityPostJpaRepository extends JpaRepository<CommunityPostEntity, Long> {

    /**
     * 국가 코드/도시 코드 기준으로 게시글을 최신순 조회한다.
     *
     * @param countryCode 국가 코드
     * @param cityCode 도시 코드
     * @return 게시글 엔티티 목록
     */
    List<CommunityPostEntity> findByCountryCodeAndCityCodeOrderByIdDesc(String countryCode, String cityCode);

    /**
     * 국가 코드/도시 코드/게시글 타입 기준으로 게시글을 최신순 조회한다.
     *
     * @param countryCode 국가 코드
     * @param cityCode 도시 코드
     * @param type     게시글 타입
     * @return 게시글 엔티티 목록
     */
    List<CommunityPostEntity> findByCountryCodeAndCityCodeAndTypeOrderByIdDesc(
            String countryCode,
            String cityCode,
            PostType type
    );
}
