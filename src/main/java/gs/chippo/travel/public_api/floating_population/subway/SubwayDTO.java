package gs.chippo.travel.public_api.floating_population.subway;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)		//DTO에 없는 필드 무시
public class SubwayDTO {

        private String CRTR_DT; // 기준_날짜
        private String ADMDONG_ID; // 행정동_ID
        private String SBWY_PSGR_CNT; // 지하철_승객_수

}
