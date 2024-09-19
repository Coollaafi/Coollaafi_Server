package coollaafi.wot.web.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Alias {
    COMMON("평범한 패피", 0, 9, "예사롭지 않은 패피", 10),
    UNUSUAL("예사롭지 않은 패피", 10, 19, "주목받는 패피", 20),
    ATTENTION("주목받는 패피", 20, 29, "독창적인 패피", 30),
    ORIGINAL("독창적인 패피", 30, 39, "세련된 패피", 40),
    ELEGANT("세련된 패피", 40, 49, "감각적인 패피", 50),
    SENSUAL("감각적인 패피", 50, 99, "스타일리시한 패피", 100),
    STYLISH("스타일리시한 패피", 100, 149, "인플루언서 패피", 150),
    INFLUENCER("인플루언서 패피", 150, 199, "패션리더 패피", 200),
    LEADER("패션리더 패피", 200, 249, "전설적인 패피", 250),
    LEGENDARY("전설적인 패피", 250, Integer.MAX_VALUE, "이미 최고 등급", 0);

    private final String alias;
    private final int minPhotos;
    private final int maxPhotos;
    private final String nextAlias;
    private final int photosForNext;

    public boolean isWithinRange(Long imageCount) {
        return imageCount >= minPhotos && imageCount <= maxPhotos;
    }

    public static Alias getAliasByImageCount(Long imageCount) {
        for (Alias alias : Alias.values()) {
            if (alias.isWithinRange(imageCount)) {
                return alias;
            }
        }
        throw new IllegalArgumentException("Invalid image count");
    }
}
