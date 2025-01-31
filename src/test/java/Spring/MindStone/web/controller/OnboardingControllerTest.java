package Spring.MindStone.web.controller;

import Spring.MindStone.web.dto.onboardingDto.OnboardingRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static Spring.MindStone.domain.enums.Job.ENGINEER;
import static Spring.MindStone.domain.enums.MBTI.INTJ;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class OnboardingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private OnboardingRequestDto validRequestDTO;

    // 테스트 메서드 실행 전
    @BeforeEach
    public void setup() {
        validRequestDTO = new OnboardingRequestDto();
        validRequestDTO.setNickname("mingdol");
        validRequestDTO.setBirthDate(java.time.LocalDate.of(2025, 5, 15));
        validRequestDTO.setJob(ENGINEER);
        validRequestDTO.setMbti(INTJ);
        validRequestDTO.setHobbies(List.of("독서", "런닝"));
        validRequestDTO.setStressManagement(List.of("운동", "명상"));
        validRequestDTO.setSpecialSkills(List.of("달리기", "코딩"));
    }

    @Test
    public void testOnboardingSubmission_Success() throws Exception {
        // given: 유효한 온보딩 요청 데이터를 JSON으로 변환
        String requestBody = objectMapper.writeValueAsString(validRequestDTO);

        // when & then: MockMvc로 POST 요청을 보내고 응답 상태 및 결과 검증
        mockMvc.perform(post("/api/onboarding/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Onboarding completed successfully"));
    }

    @Test
    public void testOnboardingSubmission_InvalidData() throws Exception {
        // given: 유효하지 않은 요청 데이터 (닉네임이 비어 있음)
        validRequestDTO.setNickname("");  // 유효하지 않은 데이터
        String requestBody = objectMapper.writeValueAsString(validRequestDTO);

        // when & then: Bad Request 응답이 반환되는지 검증
        mockMvc.perform(post("/api/members/survey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

}
