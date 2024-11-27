package coollaafi.wot.web.member.converter;

import coollaafi.wot.web.member.MemberDTO;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.ootdImage.OotdImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberConverter {
    private final OotdImageRepository ootdImageRepository;

    public MemberDTO.MemberBasedDTO toMemberBasedDTO(Member member) {
        return MemberDTO.MemberBasedDTO.builder()
                .memberId(member.getId())
                .memberServiceId(member.getServiceId())
                .memberNickName(member.getNickname())
                .memberImage(member.getProfileimage())
                .alias(member.getAlias().getAlias())
                .build();
    }

    public MemberDTO.MemberAddDTO toMemberAddDTO(Member member) {
        return MemberDTO.MemberAddDTO.builder()
                .nextAlias(member.getAlias().getNextAlias())
                .photosUntilNextAlias(
                        member.getAlias().getPhotosForNext() - ootdImageRepository.countPhotoByMember(member))
                .createdAt(member.getCreatedAt())
                .build();
    }

    public MemberDTO.MemberAllDTO toMemberAllDTO(Member member) {
        return MemberDTO.MemberAllDTO.builder()
                .memberBased(toMemberBasedDTO(member))
                .memberAdd(toMemberAddDTO(member))
                .build();
    }
}
