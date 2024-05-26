package gs.chippo.travel.board;

import gs.chippo.travel.board.BoardDTO;
import gs.chippo.travel.board.BoardEntity;
import gs.chippo.travel.user.UserEntity;
import gs.chippo.travel.board.BoardRepository;
import gs.chippo.travel.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;


    public List<BoardEntity> create(final String userId, final BoardDTO entity){
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        BoardEntity tempEntity = BoardDTO.toEntity(entity);
        tempEntity.setUser(user);
        validate(tempEntity);
        boardRepository.save(tempEntity);
        return boardRepository.findByUserId(userId);
    }


    public void validate(final BoardEntity entity) {
        if(entity == null ) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }
        if(entity.getUser() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
        log.info("Entity : {}", entity);
    }
}




