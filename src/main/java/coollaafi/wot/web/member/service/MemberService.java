package coollaafi.wot.web.member.service;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
import coollaafi.wot.s3.AmazonS3Manager;
import coollaafi.wot.web.member.MemberDTO;
import coollaafi.wot.web.member.converter.MemberConverter;
import coollaafi.wot.web.member.entity.Alias;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.member.handler.MemberHandler;
import coollaafi.wot.web.member.repository.MemberRepository;
import coollaafi.wot.web.ootdImage.OotdImageRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;
    private final OotdImageRepository ootdImageRepository;
    private final AmazonS3Manager amazonS3Manager;

    @Transactional
    public MemberDTO.joinMemberResponseDTO joinMember(MemberDTO.joinMemberDTO joinMemberDTO, MultipartFile profileImage)
            throws IOException {
        Member member = memberRepository.findById(joinMemberDTO.getMemberId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 멤버 정보 업데이트
        member.setServiceId(joinMemberDTO.getServiceId());
        member.setNickname(joinMemberDTO.getNickname());

        if (profileImage != null && !profileImage.isEmpty()) {
            // 프로필 이미지 업로드
            String profileImageUrl = amazonS3Manager.uploadFile("profile/", profileImage, member.getKakaoId());
            member.setProfileimage(profileImageUrl);
        }
        else {
            member.setProfileimage(null);
        }

        return MemberDTO.joinMemberResponseDTO.builder()
                .memberId(member.getId())
                .kakaoId(member.getKakaoId())
                .serviceId(member.getServiceId())
                .nickname(member.getNickname())
                .profileUrl(member.getProfileimage())
                .build();
    }

    public String editProfile(Long memberId, MultipartFile profileImage)
            throws IOException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        String profileImageUrl = null;

        if (profileImage != null && !profileImage.isEmpty()) {
//            amazonS3Manager.deleteFile(member.getProfileimage());
            profileImageUrl = amazonS3Manager.uploadFile("profile/", profileImage, member.getKakaoId());
        }

        member.setProfileimage(profileImageUrl);
        memberRepository.save(member);

        return profileImageUrl;
    }

    @Transactional
    public boolean isServiceIdDuplicate(String serviceId) {
        return memberRepository.existsByServiceId(serviceId);
    }

    @Transactional
    public MemberDTO.MemberAllDTO getMemberDTO(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler((ErrorStatus.MEMBER_NOT_FOUND)));
        setAlias(member);
        return memberConverter.toMemberAllDTO(member);
    }

    @Transactional
    public Void setAlias(Member member) {
        Long imageCount = ootdImageRepository.countPhotoByMember(member);

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
