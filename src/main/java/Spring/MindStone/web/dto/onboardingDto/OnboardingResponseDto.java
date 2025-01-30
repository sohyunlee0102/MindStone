package Spring.MindStone.web.dto.onboardingDto;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Schema(description = "온보딩 Response Dto")
public class OnboardingResponseDto {
    @Schema(description = "응답 코드", example = "COMMON200")
    private String code;  // 응답 코드

    @Schema(description = "응답 메시지", example = "온보딩이 성공적으로 완료되었습니다.")
    private String message; // 응답 메시지

    @Schema(description = "성공 여부", example = "true")
    private boolean isSuccess;  // 요청 성공 여부

    @Schema(description = "HTTP 상태 코드", example = "OK")
    private String httpStatus;  // Http 상태 코드


    // 성공 - 응답 처리
    public OnboardingResponseDto(SuccessStatus successStatus) {
        this.code = successStatus.getCode();
        this.message = successStatus.getMessage();
        this.isSuccess = true;
        this.httpStatus = successStatus.getHttpStatus().name();
    }


    // 실패 - 응답 처리
    public OnboardingResponseDto(ErrorStatus errorStatus) {
        this.code = errorStatus.getCode();
        this.message = errorStatus.getMessage();
        this.isSuccess = false;
        this.httpStatus = errorStatus.getHttpStatus().name();
    }
}
