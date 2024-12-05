package coollaafi.wot.web.post;

import lombok.Getter;

public enum Category {
    BLOUSE("블라우스", "blouse"),
    SHIRTS("셔츠", "shirts"),
    JACKET("자켓", "jacket"),
    HOODIE("후드집업", "hoodie"),
    COAT("코트", "coat"),
    PANTS("바지", "pants"),
    SKIRT("치마", "skirt"),
    DRESS("원피스", "dress"),
    JEAN("청바지", "jean"),
    SHOES("신발", "shoes");

    @Getter
    private final String displayName;

    @Getter
    private final String AIName;

    Category(String displayName, String AIName) {
        this.displayName = displayName;
        this.AIName = AIName;
    }
}
