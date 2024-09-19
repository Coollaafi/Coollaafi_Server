package coollaafi.wot.web.member.controller;

import coollaafi.wot.apiPayload.ApiResponse;
import coollaafi.wot.web.member.MemberDTO;
import coollaafi.wot.web.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
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
}
