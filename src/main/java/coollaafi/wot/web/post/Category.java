package coollaafi.wot.web.post;

public enum Category {
    BLOUSE("블라우스"),
    SHIRT("셔츠"),
    JACKET("자켓"),
    COAT("코트"),
    PANTS("바지"),
    SKIRT("치마"),
    DRESS("원피스"),
    SHOES("신발");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
