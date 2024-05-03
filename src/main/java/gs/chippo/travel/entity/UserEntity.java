package gs.chippo.travel.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(mappedBy = "user")
    private List<BoardEntity> boards = new ArrayList<>();
}
