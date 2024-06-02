package gs.chippo.travel.public_api.floating_population.subway;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth/test")
public class SubwayController {

    @Autowired
    private SubwayService subwayService;

    @GetMapping
    public List<SubwayDTO> test () {
        return subwayService.getSubwayServiceObject();
    }

    @GetMapping("/areacode")
    public SubwayAreaDTO JoinSubwayAndArea (@RequestParam String regionCode){
        return subwayService.JoinSubwayAndArea(regionCode);
    }
}
