package coollaafi.wot.member.repository;

import coollaafi.wot.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);

    Optional<Member> findByUid(Long uid);
}
