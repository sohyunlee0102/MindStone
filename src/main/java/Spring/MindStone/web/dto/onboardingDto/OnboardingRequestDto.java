package Spring.MindStone.web.dto.onboardingDto;

import Spring.MindStone.domain.enums.Job;
import Spring.MindStone.domain.enums.MBTI;
import Spring.MindStone.web.dto.habitDto.HabitRequestDto;
import com.fasterxml.jackson.databind.annotation.JsonValueInstantiator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class OnboardingRequestDto {
    @NotBlank(message = "닉네임은 필수 항목입니다.")
    @Schema(description = "사용자 닉네임", example = "밍돌")
    private String nickname;        // 닉네임

    @NotNull(message = "생년월일은 필수 항목입니다.")
    @Schema(description = "사용자 생년월일", example = "yyyy-mm-dd")
    private LocalDate birthDate;    // 생년월일

    @NotNull(message = "직업은 필수 항목입니다.")
    @Schema(description = "사용자 직업", example = "대학생/대학원생")
    private Job job;                // 직업

    @NotNull(message = "MBTI는 필수 항목입니다.")
    @Schema(description = "사용자 mbti", example = "ENTJ")
    private MBTI mbti;              // MBTI

    @NotEmpty(message = "스트레스 관리 방법은 필수 항목입니다.")
    @Schema(description = "스트레스 관리 방법", example = "독서, 운동")
    private List<String> stressManagement;  // 스트레스 관리 방법 (필수)

    @NotEmpty(message = "취미는 필수 항목입니다.")
    @Schema(description = "취미", example = "운동, 독서")
    private List<String> hobbies;           // 취미 (필수)

    @NotEmpty(message = "특기는 필수 항목입니다.")
    @Schema(description = "특기", example = "요리, 글쓰기")
    private List<String> specialSkills;     // 특기 (필수)


    // 추가 선택 사항 - 만들고 싶은 습관
    @Valid
    @Schema(description = "습관")
    private List<HabitRequestDto.HabitDto> habits;            // 습관 - 습관이 있을 때만 각 항목 유효성 검증
}
