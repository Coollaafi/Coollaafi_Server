package coollaafi.wot.web.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberDTO{
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberBasedDTO{
        private String memberName;
        private String memberImage;
        private String alias;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberAddDTO{
        private String nextAlias;
        private Long photosUntilNextAlias;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberAllDTO{
        private MemberBasedDTO memberBased;
        private MemberAddDTO memberAdd;
    }
}