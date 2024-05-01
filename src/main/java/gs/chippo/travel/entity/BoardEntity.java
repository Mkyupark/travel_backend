package gs.chippo.travel.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
