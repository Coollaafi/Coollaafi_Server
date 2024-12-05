package coollaafi.wot.web.postPrefer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostPreferRequestDTO {
    private Long postId;
    private Long memberId;
}
