package Spring.MindStone.web.dto.memberInfoDto;

import Spring.MindStone.domain.enums.Job;
import Spring.MindStone.domain.enums.MBTI;
import Spring.MindStone.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class MemberInfoRequestDTO {

    @Getter
    @Setter
    public static class JoinDto {

        @NotBlank
        @Email
        String email;
        @NotBlank
        String password;
        @NotBlank
        String nickname;
        MBTI mbti;
        @NotNull
        LocalDate birthday;
        Job job;
        @NotNull
        Boolean shareScope;
        @NotNull
        Boolean marketingAgree;
        @NotNull
        Role role;
    }

    @Getter
    @Setter
    public static class LoginDto {

        @NotBlank
        @Email
        String email;
        @NotBlank
        String password;
    }

    @Getter
    @Setter
    public static class RefreshTokenDTO {

        @NotNull
        private String refreshToken;
        @NotBlank
        @Email
        String email;
    }

    @Getter
    @Setter
    public static class EmailDto {
        @NotBlank
        @Email
        String email;
    }

    @Getter
    @Setter
    public static class VerificationDto {
        @NotBlank
        @Email
        String email;
        @NotNull
        String code;
    }

    @Getter
    @Setter
    public static class NicknameDto {
        @NotBlank
        String nickname;
    }

    @Getter
    @Setter
    public static class PasswordDto {
        @NotBlank
        String oldPassword;
        @NotBlank
        String newPassword;
    }

}
