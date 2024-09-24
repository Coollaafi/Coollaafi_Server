package coollaafi.wot.web.member.service;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
import coollaafi.wot.web.member.converter.MemberConverter;
import coollaafi.wot.web.member.MemberDTO;
import coollaafi.wot.web.member.entity.Alias;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.member.handler.MemberHandler;
import coollaafi.wot.web.member.repository.MemberRepository;
import coollaafi.wot.web.photo.PhotoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;
    private final PhotoRepository photoRepository;

    @Transactional
    public Member getMemberByUid(Long uid){
        return memberRepository.findByUid(uid)
                .orElseThrow(()->new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Transactional
    public MemberDTO.MemberAllDTO getMemberDTO(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler((ErrorStatus.MEMBER_NOT_FOUND)));
        setAlias(member);
        return memberConverter.toMemberAllDTO(member);
    }

    @Transactional
    public Void setAlias(Member member){
        Long imageCount = photoRepository.countPhotoByMember(member);

        // 이미지 개수를 기준으로 Alias를 가져옴
        Alias alias = Alias.getAliasByImageCount(imageCount);

        // 현재 별명 설정
        member.setAlias(alias);

        memberRepository.save(member);
        return null;
    }

    @Transactional
    public List<MemberDTO.MemberBasedDTO> searchMembers(String searchTerm) {
        List<Member> searchMembers = memberRepository.findByNicknameOrUserIdStartsWith(searchTerm);
        return searchMembers.stream()
                .map(memberConverter::toMemberBasedDTO).collect(Collectors.toList());
    }

    // 특정 멤버의 친구 목록을 조회
    @Transactional
    public List<MemberDTO.MemberBasedDTO> getFriends(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        return member.getFriends().stream()
                .map(memberConverter::toMemberBasedDTO).collect(Collectors.toList());
    }
}
