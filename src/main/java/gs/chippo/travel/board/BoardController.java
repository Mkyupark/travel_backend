package gs.chippo.travel.board;


import gs.chippo.travel.dto.ResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.stream.Collectors;


@Log4j2
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/board")
public class BoardController {


    @Autowired
    private BoardService boardService;

    @PostMapping
                                            // authenticationPrincipal 사용이유
    // front에서 userId를 계속해서 입력해줄 필요없이 Token 값으로 현재 userId를 판별가능하다
    public ResponseEntity<?> createBoard (@AuthenticationPrincipal String userId, @RequestBody BoardDTO boardDTO){
        try{
            // dto를 이용하여 테이블에 저장하기 위한  entity  생성 => FK 주입해주어야함
            log.info("userId : {} ", userId);
            BoardEntity entity = BoardDTO.toEntity(boardDTO);
            log.info("Entity : {}", entity);
            entity.setId(null);
            //entity.setUser(userId=> 이거 userRepository에서 찾아주어야함.);

            List<BoardEntity> entities = boardService.create(userId, boardDTO);


            List<BoardDTO> dtos = entities.stream().map(BoardDTO::new).collect(Collectors.toList());
            ResponseDTO<BoardDTO> response = ResponseDTO.<BoardDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
            // HTTP Status 200 상태로 response 를 전송한다. return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<BoardDTO> response = ResponseDTO.<BoardDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }

    }

}
