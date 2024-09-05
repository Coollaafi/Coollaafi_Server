package coollaafi.wot.web.postPrefer;

import coollaafi.wot.web.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostPreferRepository extends JpaRepository<PostPrefer, Long> {
    List<PostPrefer> findByMember(Member member);
}
