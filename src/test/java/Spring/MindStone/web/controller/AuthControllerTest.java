package Spring.MindStone.web.controller;

import Spring.MindStone.web.dto.MemberInfoRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String accessToken;
    private String refreshToken;
    private String email = "user@example.com";

    @BeforeEach
    public void loginAndGetToken() throws Exception {
        MemberInfoRequestDTO.LoginDto loginDto = new MemberInfoRequestDTO.LoginDto();
        loginDto.setEmail(email);
        loginDto.setPassword("password");

        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 로그인 요청을 보내고 응답을 JSON 문자열로 받음
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto))) // ObjectMapper 사용하여 JSON 직렬화
                .andReturn().getResponse().getContentAsString();

        // 응답을 JsonNode로 변환하여 accessToken을 직접 추출
        JsonNode rootNode = objectMapper.readTree(response);
        accessToken = rootNode.path("result").path("accessToken").asText();  // 직접 accessToken 추출
        refreshToken = rootNode.path("result").path("refreshToken").asText();

        // 토큰을 확인 (옵션)
        System.out.println("Access Token: " + accessToken);
        System.out.println("Refresh Token: " + refreshToken);
    }

    @Test
    public void testAccessWithValidToken() throws Exception {
        mockMvc.perform(get("/api/test/auth/testEndpoint")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testRefreshToken() throws Exception {
        Thread.sleep(11000);

        mockMvc.perform(get("/api/test/auth/testEndpoint")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://localhost/login"));

        System.out.println("Access Token has expired. AccessToken will be newly created.");

        MemberInfoRequestDTO.RefreshTokenDTO refreshTokenDTO = new MemberInfoRequestDTO.RefreshTokenDTO();
        refreshTokenDTO.setRefreshToken(refreshToken);
        refreshTokenDTO.setEmail(email);

        String response = mockMvc.perform(post("/api/auth/refreshToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode rootNode = objectMapper.readTree(response);
        String newAccessToken = rootNode.path("result").path("accessToken").asText();

        System.out.println("New Access Token: " + newAccessToken);

        mockMvc.perform(get("/api/test/auth/testEndpoint")
                        .header("Authorization", "Bearer " + newAccessToken))
                .andExpect(status().isOk());
    }

}