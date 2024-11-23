package coollaafi.wot.web.member.controller;

import coollaafi.wot.apiPayload.ApiResponse;
import coollaafi.wot.web.member.MemberDTO;
import coollaafi.wot.web.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    @GetMapping("/healthcheck")
    public String healthcheck() {
        return "OK";
    }

    @PostMapping(value = "/join", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "회원가입 API", description = "회원가입시 필요한 API입니다. 카카오 로그인 이후 호출해주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<MemberDTO.joinMemberResponseDTO> joinMember(
            @RequestPart("joinMemberDTO") MemberDTO.joinMemberDTO joinMemberDTO,
            @RequestPart(name = "profileImage", required = false) MultipartFile profileImage) {// 예외 로그 추가
        try {
            MemberDTO.joinMemberResponseDTO result = memberService.joinMember(joinMemberDTO, profileImage);
            return ApiResponse.onSuccess(result);
        } catch (Exception e) {
            log.error("회원가입 처리 중 오류 발생: ", e);
            return ApiResponse.onFailure("500", "회원가입 중 오류가 발생했습니다.", null);
        }
    }

    @PostMapping(value = "/edit-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "사용자 프로필 사진 변경 API", description = "사용자의 프로필 사진을 변경하는 API입니다. 기본 이미지로 변경 시 profileImage를 넣지 않고 호출해주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<String> editProfile(
            @RequestParam("memberId") Long memberId,
            @RequestParam(name = "profileImage", required = false) MultipartFile profileImage) {
        try {
            String result = memberService.editProfile(memberId, profileImage);
            return ApiResponse.onSuccess(result);
        } catch (Exception e) {
            log.error("프로필 변경 중 오류 발생: ", e);
            return ApiResponse.onFailure("500", "프로필 변경 중 오류가 발생했습니다.", null);
        }
    }

    @GetMapping("/check-service-id")
    @Operation(summary = "서비스 ID 중복 검사", description = "회원가입 시 serviceId 중복 여부를 확인하는 API입니다. 중복일 경우에 true가 return 됩니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ApiResponse<Boolean> checkServiceId(@RequestParam("serviceId") String serviceId) {
        boolean isDuplicate = memberService.isServiceIdDuplicate(serviceId);
        return ApiResponse.onSuccess(isDuplicate);
    }

    @Operation(
            summary = "홈화면-멤버정보 조회 API",
            description = "홈화면에서 필요한 멤버 정보를 조회하는 API입니다.",
            parameters = @Parameter(name = "memberId", description = "조회할 멤버 id")
    )
    @GetMapping("/{memberId}")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<MemberDTO.MemberAllDTO> getMemberDTO(@PathVariable("memberId") Long memberId) {
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
    public ApiResponse<List<MemberDTO.MemberBasedDTO>> getFriends(@PathVariable("memberId") Long memberId) {
        List<MemberDTO.MemberBasedDTO> friends = memberService.getFriends(memberId);
        return ApiResponse.onSuccess(friends);
    }
}
