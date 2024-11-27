package coollaafi.wot.web.member.entity;

import coollaafi.wot.common.BaseEntity;
import coollaafi.wot.web.collageImage.CollageImage;
import coollaafi.wot.web.comment.Comment;
import coollaafi.wot.web.friendRequest.FriendRequest;
import coollaafi.wot.web.friendship.Friendship;
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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<FriendRequest> sendRequests;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<FriendRequest> receivedRequests;

    @OneToMany(mappedBy = "member1", cascade = CascadeType.ALL)
    private List<Friendship> friendships1;

    @OneToMany(mappedBy = "member2", cascade = CascadeType.ALL)
    private List<Friendship> friendships2;

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

    public Set<Member> getFriends() {
        return Stream.concat(
                friendships1.stream().map(Friendship::getMember2),
                friendships2.stream().map(Friendship::getMember1)
        ).collect(Collectors.toSet());
    }
}
