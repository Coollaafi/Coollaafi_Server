package coollaafi.wot.web.postPrefer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostPreferResponseDTO {
    Long postPreferId;
    LocalDateTime createdAt;
}
