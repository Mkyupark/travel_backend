package gs.chippo.travel.user;

import gs.chippo.travel.board.BoardEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;


// 이거 ToString exclude 작성하지 않으면 순환참조가 생김
// 참조할시 서로 서로 계속해서 참조하는 오류가 생긴다.
@ToString(exclude = "boards")
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String id;

    @Column(nullable=false)
    private String username;

    @Column(nullable=false)
    private String email;

    @Column(nullable=false)
    private String password;

    // mappedBy = user => 이거 boardEntity에 적어놓은 이름이랑 똑같이 맞춰야함
    // 양방향 mapping
    @OneToMany(mappedBy = "user")
    private List<BoardEntity> boards = new ArrayList<>();
}
