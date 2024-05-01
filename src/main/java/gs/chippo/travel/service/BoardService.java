package gs.chippo.travel.service;

import gs.chippo.travel.entity.BoardEntity;
import gs.chippo.travel.repository.BoardRepository;
import gs.chippo.travel.repository.UserRepository;
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


    public List<BoardEntity> create(final String userId, final BoardEntity entity){
        BoardEntity tempEntity = entity.builder().user(userRepository.getById(userId)).build();
        validate(entity);
        boardRepository.save(entity);
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




