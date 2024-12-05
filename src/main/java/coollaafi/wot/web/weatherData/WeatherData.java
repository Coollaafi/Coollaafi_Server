package coollaafi.wot.web.weatherData;

import coollaafi.wot.web.collageImage.CollageImage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Date;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class WeatherData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;
    private Float tavg;             // 일별 평균 기온
    private Float tmin;             // 일별 최저 기온
    private Float tmax;             // 일별 최고 기온
    private Float prcp;             // 일별 강수량
    private Float snow;             // 일별 강설량
    private Float wdir;             // 일별 풍향
    private Float wspd;             // 일별 평균풍속
    private Float pres;             // 일별 해수면 기압

    @OneToMany(mappedBy = "weatherData", cascade = CascadeType.ALL)
    private List<CollageImage> collageImages;
}
