package Spring.MindStone.web.dto.noticeDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeRequestDto {

    @Schema(description = "공지 제목", example = "시스템 점검 안내")
    private String title;

    @Schema(description = "공지 내용", example = "1월 20일 00:00~05:00까지 시스템 점검이 진행됩니다.")
    private String content;
}
