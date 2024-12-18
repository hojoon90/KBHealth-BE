package com.healthcare.kb.filter;

import com.healthcare.kb.exception.AuthorizeException;
import com.healthcare.kb.service.KbhcSecurityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Optional;

import static com.healthcare.kb.constant.MessageConst.UNAUTHORIZED_TOKEN;


@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final KbhcSecurityService kbhcSecurityService;
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) {
        try{
            log.debug("doFilterInternal() 호출");

            Optional<String> jwtToken = kbhcSecurityService.extractAccessToken(request);
            if(jwtToken.isPresent()){
                jwtToken.filter(kbhcSecurityService::isTokenValid)
                        .flatMap(kbhcSecurityService::getUserDetails)
                        .ifPresent(this::saveAuthentication);
            }

            filterChain.doFilter(request, response);
        }catch (Exception e){
            throw new AuthorizeException(UNAUTHORIZED_TOKEN);
        }
    }

    public void saveAuthentication(UserDetails userDetailsUser) {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
