package gs.chippo.travel.service;

import gs.chippo.travel.dto.BoardDTO;
import gs.chippo.travel.entity.BoardEntity;
import gs.chippo.travel.entity.UserEntity;
import gs.chippo.travel.repository.BoardRepository;
import gs.chippo.travel.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
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




