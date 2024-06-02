package gs.chippo.travel.public_api.area_code;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AreaCodeRepository extends JpaRepository<AreaCodeEntity, Long> {
    List<AreaCodeEntity> findByRegionCode(String regionCode);

}
