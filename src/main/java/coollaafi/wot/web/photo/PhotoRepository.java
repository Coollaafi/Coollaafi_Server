package coollaafi.wot.web.photo;

import coollaafi.wot.web.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByMember(Member member);
    Long countByMember(Member member);
}
