package coollaafi.wot.web.post;

import coollaafi.wot.common.BaseEntity;
import coollaafi.wot.web.comment.Comment;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.reply.Reply;
import coollaafi.wot.web.postPrefer.PostPrefer;
import jakarta.persistence.*;
import lombok.*;

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

    private String ootdImage;
    private String lookbookImage;
    private String description;
    private PostCondition postCondition;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostPrefer> postPrefers;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
