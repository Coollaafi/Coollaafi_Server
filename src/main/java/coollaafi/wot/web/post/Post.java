package coollaafi.wot.web.post;

import coollaafi.wot.common.BaseEntity;
import coollaafi.wot.web.comment.Comment;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.ootdImage.OotdImage;
import coollaafi.wot.web.postPrefer.PostPrefer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToOne
    @JoinColumn(name = "ootd_image_id")
    private OotdImage ootdImage;

    private String lookbookImage;
    private String description;
    private PostCondition postCondition;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostPrefer> postPrefers;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
