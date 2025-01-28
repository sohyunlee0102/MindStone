package Spring.MindStone.web.controller;

import Spring.MindStone.apiPayload.ApiResponse;
import Spring.MindStone.web.dto.memberInfoDto.MemberInfoRequestDTO;
import Spring.MindStone.web.dto.memberInfoDto.MemberInfoResponseDTO;
import Spring.MindStone.service.MemberInfoService.MemberInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Validated
public class MemberInfoController {

    private final MemberInfoService memberInfoService;

    @PostMapping
    @Operation(summary = "회원가입 API", description = "유저 회원가입을 처리하는 API 입니다.")
    @Parameters({
            @Parameter(name = "email", description = "유저의 이메일, Request Body 입니다!"),
            @Parameter(name = "password", description = "유저의 비밀번호, Request Body 입니다!")
    })
    public ApiResponse<MemberInfoResponseDTO.JoinResultDTO> join(@Valid @RequestBody MemberInfoRequestDTO.JoinDto request) {
        return ApiResponse.onSuccess(memberInfoService.joinMember(request));
    }

}
