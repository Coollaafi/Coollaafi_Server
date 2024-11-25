package coollaafi.wot.web.postPrefer;

import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.post.Post;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostPreferConverter {
    public PostPreferResponseDTO toResultDTO(PostPrefer postPrefer) {
        return PostPreferResponseDTO.builder()
                .postPreferId(postPrefer.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public PostPrefer toEntity(Post post, Member member) {
        return PostPrefer.builder()
                .post(post)
                .member(member)
                .build();
    }
}
