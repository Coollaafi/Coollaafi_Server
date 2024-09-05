package coollaafi.wot.web.post;

import coollaafi.wot.common.BaseEntity;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.postPrefer.PostPrefer;
import jakarta.persistence.*;
import lombok.*;

import java.net.URL;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private URL ootdImage;
    private URL lookbookImage;
    private String description;
    private PostCondition postCondition;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostPrefer> postPrefers;

    // 특정 member가 이 post를 볼 수 있는지 확인
    public boolean isVisibleTo(Member member){
        return this.member.getFriends().contains(member) || this.member.equals(member);
    }
}
