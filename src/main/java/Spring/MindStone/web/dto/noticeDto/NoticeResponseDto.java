package Spring.MindStone.web.dto.noticeDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NoticeResponseDto {

    @Schema(description = "공지사항 ID", example = "1")
    private Long id;

    @Schema(description = "공지 제목", example = "시스템 점검 안내")
    private String title;

    @Schema(description = "공지 내용", example = "1월 20일 00:00~05:00까지 시스템 점검이 진행됩니다.")
    private String content;

    @Schema(description = "공지 등록 시간", example = "2025-02-18T12:00:00")
    private LocalDateTime createdAt;
}
