package coollaafi.wot.web.member.entity;

import coollaafi.wot.common.BaseEntity;
import coollaafi.wot.web.friendRequest.FriendRequest;
import coollaafi.wot.web.friendship.Friendship;
import coollaafi.wot.web.post.Post;
import coollaafi.wot.web.postPrefer.PostPrefer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Post> posts;

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

    public Set<Member> getFriends(){
        return Stream.concat(
                friendships1.stream().map(Friendship::getMember2),
                friendships2.stream().map(Friendship::getMember1)
        ).collect(Collectors.toSet());
    }
}
