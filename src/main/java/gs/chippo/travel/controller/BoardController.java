package gs.chippo.travel.controller;


import gs.chippo.travel.dto.BoardDTO;
import gs.chippo.travel.dto.ResponseDTO;
import gs.chippo.travel.entity.BoardEntity;
import gs.chippo.travel.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
            BoardEntity entity = BoardDTO.toEntity(boardDTO);
            entity.setId(null);
            //entity.setUser(userId=> 이거 userRepository에서 찾아주어야함.);

            List<BoardEntity> entities = boardService.create(userId, entity);


            List<BoardDTO> dtos = entities.stream().map(BoardDTO::new).collect(Collectors.toList());
            ResponseDTO<BoardDTO> response = ResponseDTO.<BoardDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body("hi");
            // HTTP Status 200 상태로 response 를 전송한다. return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<BoardDTO> response = ResponseDTO.<BoardDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }

    }
}
