package gs.chippo.travel.public_api.floating_population.subway;

import com.fasterxml.jackson.databind.JsonNode;
import gs.chippo.travel.config.RestTemplateConfig;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
@Log4j2
@Service
public class SubwayService {
    @Autowired
    private ObjectMapper objectMapper;
    private final String HOST_URL = "http://openapi.seoul.go.kr:8088";
    private final String API_KEY = "78464277667061723630586e6f4974";
    private final String TYPE = "json";
    private final String SERVICE_NAME = "tpssSubwayPassenger";
    private final int START_INDEX = 1;
    private final int END_INDEX = 100;
    @Autowired
    private RestTemplateConfig restTemplateConfig;

    public List<SubwayDTO> getSubwayServiceObject() {
        // 결과를 저장할 StringBuffer 초기화
        StringBuffer result = new StringBuffer();

        try {
            // 봉사 정보를 조회하기 위한 API URL 생성
            String API_URL = String.format("%s/%s/%s/%s/%d/%d", HOST_URL, API_KEY, TYPE, SERVICE_NAME, START_INDEX, END_INDEX);

            // API에 연결하기 위한 URL 객체 생성
            // 이 부분에서는 apiUrl 문자열을 사용하여 URL 객체를 생성. 이 URL 객체는 나중에 HttpURLConnection에 사용됩니다.
            URL url = new URL(API_URL);

            // HttpURLConnection 객체를 생성. 이 객체를 사용하여 API에 연결.
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // 이 단계에서 실제 네트워크 통신이 발생, API 서버에 연결 요청.
            urlConnection.connect();

            // API 응답을 읽기 위한 스트림 설정
            // BufferedInputStream은 byte 단위로 데이터를 읽어들이는 스트림입니다.
            // urlConnection.getInputStream()을 통해 API의 응답 데이터를 스트림 형태로 가져옵니다.
            BufferedInputStream bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());

            // BufferedReader는 문자(character) 단위로 데이터를 읽어들이는 스트림입니다.
            // InputStreamReader를 사용하여 byte 스트림을 character 스트림으로 변환합니다.
            // "UTF-8"은 문자 인코딩을 나타내며, 대부분의 웹 API 응답은 UTF-8로 인코딩됩니다.
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream, StandardCharsets.UTF_8));

            // 이 while 문은 API의 응답 데이터를 한 줄씩 읽어들입니다.
            // 응답 데이터가 더 이상 없을 때까지 반복하여 읽어들이며, 각 줄을 result StringBuffer 객체에 추가합니다.
            String returnLine = null;
            while ((returnLine = bufferedReader.readLine()) != null) {
                result.append(returnLine);
               // log.info(returnLine);
            }
            log.info("API Response: " + result.toString());

            // JSON 데이터를 SubwayDTO로 변환
            JsonNode rootNode = objectMapper.readTree(result.toString());
            JsonNode rowNode = rootNode.path("tpssSubwayPassenger").path("row");
            log.info("rowNode : " + rowNode);

            // "row" 값을 SubwayDTO로 변환
            List<SubwayDTO> subwayList = new ArrayList<SubwayDTO>();

            for (JsonNode row : rowNode) {
                log.info(row.toString());
                String crtrDt = row.path("CRTR_DT").asText();
                String admongId = row.path("ADMDONG_ID").asText();
                String sbwyPsgrCnt = row.path("SBWY_PSGR_CNT").asText();
                SubwayDTO subwayPassenger = new SubwayDTO();
                subwayPassenger.setCRTR_DT(crtrDt);
                subwayPassenger.setADMDONG_ID(admongId);
                subwayPassenger.setSBWY_PSGR_CNT(sbwyPsgrCnt);

                log.info(subwayPassenger);
                subwayList.add(subwayPassenger);
            }
            log.info("Converted DTO: " + subwayList);
            // API 응답 데이터를 XML에서 JSONObject으로 변환
            return subwayList;

        } catch (Exception e) {
            List<SubwayDTO> subwayDTO = new ArrayList<SubwayDTO>();
            log.error("Api 호출 오류", e);
            return subwayDTO; // 빈 JSON 객체 반환
        }
    }


}