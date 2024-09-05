package coollaafi.wot.web.member.Service;

import coollaafi.wot.web.member.entity.Member;

public interface MemberQueryService {
    Member getMemberByUid(Long uid);
}
