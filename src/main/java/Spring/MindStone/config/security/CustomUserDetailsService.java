package Spring.MindStone.config.security;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.handler.AuthHandler;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.repository.MemberInfoRepository;
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
        System.out.println(username);
        MemberInfo memberInfo = memberInfoRepository.findByEmail(username)
                .orElseThrow(() -> new AuthHandler(ErrorStatus.AUTHENTICATION_FAILED));

         System.out.println(memberInfo.getPassword());

        return org.springframework.security.core.userdetails.User
                .withUsername(memberInfo.getEmail())
                .password(memberInfo.getPassword())
                .roles(memberInfo.getRole().name())
                .build();
    }

}
