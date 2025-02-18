package Spring.MindStone.service.noticeService;

import Spring.MindStone.domain.notice.Notice;
import Spring.MindStone.repository.noticeRepository.NoticeRepository;
import Spring.MindStone.web.dto.noticeDto.NoticeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;


    // 모든 공지사항 조회
    @Transactional(readOnly = true)
    public List<NoticeResponseDto> getAllNotices() {
        return noticeRepository.findAll().stream()
                .map(notice -> new NoticeResponseDto(notice.getId(), notice.getTitle(), notice.getContent(), notice.getCreatedAt()))
                .collect(Collectors.toList());
    }

    // 특정 공지사항 상세 조회
    @Transactional(readOnly = true)
    public NoticeResponseDto getNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항을 찾을 수 없습니다. ID: " + id));

        return new NoticeResponseDto(notice.getId(), notice.getTitle(), notice.getContent(), notice.getCreatedAt());
    }
}
