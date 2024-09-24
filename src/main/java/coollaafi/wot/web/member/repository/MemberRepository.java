package coollaafi.wot.web.member.repository;

import coollaafi.wot.web.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);

    Optional<Member> findByUid(Long uid);


    @Query("SELECT m FROM Member m WHERE m.nickname LIKE :searchTerm% OR m.serviceId LIKE :searchTerm%")
    List<Member> findByNicknameOrUserIdStartsWith(@Param("searchTerm") String searchTerm);
}
