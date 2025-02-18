package Spring.MindStone.web.controller;

import Spring.MindStone.service.noticeService.NoticeService;
import Spring.MindStone.web.dto.noticeDto.NoticeResponseDto;
import Spring.MindStone.config.jwt.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
@Tag(name = "Notice", description = "공지사항 API")
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 목록 조회", description = "모든 공지사항을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<NoticeResponseDto>> getAllNotices(
            @RequestHeader("Authorization") String authorization) { // ✅ 헤더에서 토큰 받기
        JwtTokenUtil.extractMemberId(authorization); // ✅ 토큰 검증 (필요하면 사용)
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    @Operation(summary = "특정 공지사항 상세 조회", description = "특정 공지사항을 상세 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponseDto> getNotice(
            @RequestHeader("Authorization") String authorization, // ✅ 헤더에서 토큰 받기
            @PathVariable Long id) {
        JwtTokenUtil.extractMemberId(authorization); // ✅ 토큰 검증 (필요하면 사용)
        return ResponseEntity.ok(noticeService.getNotice(id));
    }
}
