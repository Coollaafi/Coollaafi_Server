package coollaafi.wot.web.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDTO {
    Long memberId;
    URL profile_url;
    String name;
}
