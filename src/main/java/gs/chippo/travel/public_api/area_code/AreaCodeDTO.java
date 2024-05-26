package gs.chippo.travel.public_api.area_code;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // dto에 없는 필드 무시
public class AreaCodeDTO {
    private String id;
    private String city;
    private String district;
    private String districtCode;
    private String region;
    private String regionCode;
    private String englishName;
}
