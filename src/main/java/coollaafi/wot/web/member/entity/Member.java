package coollaafi.wot.web.member.entity;

import coollaafi.wot.common.BaseEntity;
import coollaafi.wot.web.collageImage.CollageImage;
import coollaafi.wot.web.comment.Comment;
import coollaafi.wot.web.friendship.Follow;
import coollaafi.wot.web.ootdImage.OotdImage;
import coollaafi.wot.web.post.Post;
import coollaafi.wot.web.postPrefer.PostPrefer;
import coollaafi.wot.web.reply.Reply;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Long kakaoId;

    private String serviceId;

    private String nickname;

    private String profileimage;

    private Alias alias;

    @Column(unique = true)
    private String accessToken;

    private String refreshToken;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Reply> replies;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<PostPrefer> postPrefers;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private List<Follow> follow1;

    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL)
    private List<Follow> follow2;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<CollageImage> collageImages;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<OotdImage> ootdImages;

    // 엔티티 저장 전에 호출되는 메서드
    @PrePersist
    public void prePersist() {
        if (this.alias == null) {
            this.alias = Alias.COMMON; // 기본 별명 설정
        }
    }
}
