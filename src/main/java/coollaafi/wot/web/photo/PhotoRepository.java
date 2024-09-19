package coollaafi.wot.web.photo;

import coollaafi.wot.web.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Query("SELECT COUNT(p) FROM Photo p WHERE p.member = :member")
    Long countPhotoByMember(@Param("member") Member member);
}
