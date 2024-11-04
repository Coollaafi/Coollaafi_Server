package coollaafi.wot.web.member.controller;

import coollaafi.wot.apiPayload.ApiResponse;
import coollaafi.wot.web.member.MemberDTO;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.io.IOException;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping(value = "/join", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "회원가입 API", description = "회원가입시 필요한 API입니다. 카카오 로그인 이후 호출해주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public Member joinMember(MemberDTO.joinMemberDTO joinMemberDTO,
                             @RequestParam("profileImage") MultipartFile profileImage) throws IOException {
        return memberService.joinMember(joinMemberDTO, profileImage);
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "홈화면-멤버정보 조회 API", description = "홈화면에서 필요한 멤버 정보를 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<MemberDTO.MemberAllDTO> getMemberDTO(@PathVariable Long memberId) {
        MemberDTO.MemberAllDTO memberDTO = memberService.getMemberDTO(memberId);
        return ApiResponse.onSuccess(memberDTO);
    }

    @GetMapping("/search")
    @Operation(summary = "친구 검색 API", description = "친구 요청을 위해 검색할 때 사용하는 API입니다. 해당 query로 시작하는 nickname, serviceId를 가진 멤버 정보가 조회됩니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<List<MemberDTO.MemberBasedDTO>> searchMembers(@RequestParam("query") String query) {
        List<MemberDTO.MemberBasedDTO> members = memberService.searchMembers(query);
        return ApiResponse.onSuccess(members);
    }

    // 친구 목록을 조회하는 API
    @GetMapping("/{memberId}/friends")
    @Operation(summary = "친구 목록 조회 API", description = "친구 목록 조회시 사용하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<List<MemberDTO.MemberBasedDTO>> getFriends(@PathVariable Long memberId) {
        List<MemberDTO.MemberBasedDTO> friends = memberService.getFriends(memberId);
        return ApiResponse.onSuccess(friends);
    }
}
