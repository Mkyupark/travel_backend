package gs.chippo.travel.repository;

import gs.chippo.travel.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity,String> {
    List<BoardEntity> findByUserId(String userId);
}
