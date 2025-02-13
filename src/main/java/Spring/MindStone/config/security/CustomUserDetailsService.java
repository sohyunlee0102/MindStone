package Spring.MindStone.config.security;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.handler.AuthHandler;
import Spring.MindStone.apiPayload.exception.handler.MemberInfoHandler;
import Spring.MindStone.domain.enums.Status;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.repository.memberRepository.MemberInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberInfoRepository memberInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        MemberInfo memberInfo = memberInfoRepository.findByEmail(username)
                .orElseThrow(() -> {
                    return new AuthHandler(ErrorStatus.AUTHENTICATION_FAILED);
                });

        if (memberInfo.getStatus() == Status.INACTIVE) {
            throw new MemberInfoHandler(ErrorStatus.INACTIVE_MEMBER);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(memberInfo.getEmail())
                .password(memberInfo.getPassword())
                .roles(memberInfo.getRole().name())
                .build();
    }

}
