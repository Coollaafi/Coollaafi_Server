package coollaafi.wot.web.photo;

import coollaafi.wot.web.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String s3Url;

    // GPS 정보
    private Double latitude;
    private Double longitude;

    private Date uploadDate; // 사진 업로드 날짜
    private Date photoDate; // 사진의 실제 촬영 날짜

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
