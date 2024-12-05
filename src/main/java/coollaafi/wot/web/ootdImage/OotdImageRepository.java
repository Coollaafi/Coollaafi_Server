package coollaafi.wot.web.ootdImage;

import coollaafi.wot.web.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OotdImageRepository extends JpaRepository<OotdImage, Long> {
    @Query("SELECT COUNT(p) FROM OotdImage p WHERE p.member = :member")
    Long countPhotoByMember(@Param("member") Member member);
}
