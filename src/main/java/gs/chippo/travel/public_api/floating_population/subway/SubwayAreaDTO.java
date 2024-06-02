package gs.chippo.travel.public_api.floating_population.subway;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SubwayAreaDTO {
    private int SBWY_PSGR_CNT; // 지하철 총 승객수
    private String city;
    private String district;
    private String districtCode;
    private String region;
    private String regionCode;
    private String englishName;
}
