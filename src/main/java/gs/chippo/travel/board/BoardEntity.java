package gs.chippo.travel.board;


import gs.chippo.travel.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@ToString(exclude = "user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table (name = "board")
public class BoardEntity {
    @Id
    @GeneratedValue(generator="system-uuid") // 자동으로 id 성성
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String id;

    private String title;

    private String content;

    private boolean fix;

    // 외래키 설정
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
