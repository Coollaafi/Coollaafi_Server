package coollaafi.wot.member.entity;

import coollaafi.wot.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(unique = true)
    private Long uid;

    private String name;

    private URL profileimage;

    @Column(unique = true)
    private String accessToken;
}
