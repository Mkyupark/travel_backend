package gs.chippo.travel.public_api.area_code;

import gs.chippo.travel.public_api.area_code.AreaCodeEntity;
import gs.chippo.travel.public_api.area_code.AreaCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaCodeService {

    @Autowired
    private AreaCodeRepository areaCoderepository;

}
