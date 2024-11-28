package coollaafi.wot.web.collageImage;

import coollaafi.wot.web.member.entity.Member;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollageImageRepository extends JpaRepository<CollageImage, Long> {
    List<CollageImage> findCollageImageByMemberAndWeatherData_Date(Member member, Date weatherDataDate);
}