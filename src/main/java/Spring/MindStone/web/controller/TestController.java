package Spring.MindStone.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/auth/testEndpoint")
    public String testEndpoint() {
        System.out.println("JWT 토큰 테스트를 위한 컨트롤러입니다.");
        return "testEndpoint";
    }

}
