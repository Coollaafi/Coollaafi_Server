package coollaafi.wot.web.post;

import lombok.*;

import java.util.List;

@Setter
public class CalendarDTO {
    private int year;
    private int month;
    private List<DayDTO> days;
    public void addDay(int day, String lookbookImage){
        days.add(new DayDTO(day, lookbookImage));
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DayDTO{
        private int day;
        private String lookbookImage;
    }
}
