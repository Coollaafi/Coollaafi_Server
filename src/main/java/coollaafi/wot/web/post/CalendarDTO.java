package coollaafi.wot.web.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class CalendarDTO {
    private List<Day> days;

    // CalendarDTO의 생성자에서 days 리스트 초기화
    public CalendarDTO() {
        this.days = new ArrayList<>();
    }

    // Day를 추가하는 메서드
    public void addDay(Day day) {
        this.days.add(day);
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Day {
        @JsonProperty("day")
        private LocalDate day;

        @JsonProperty("lookbookImage")
        private String lookbookImage;

        private Long postId;
    }
}