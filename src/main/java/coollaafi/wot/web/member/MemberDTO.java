package coollaafi.wot.web.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO{
    private String memberName;
    private String memberImage;
    private String alias;
}