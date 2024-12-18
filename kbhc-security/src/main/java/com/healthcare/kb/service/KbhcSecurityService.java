package com.healthcare.kb.service;

import com.healthcare.kb.dto.JwtDto;
import com.healthcare.kb.dto.LoginUser;
import com.healthcare.kb.exception.AuthorizeException;
import com.healthcare.kb.type.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import com.healthcare.kb.dto.AppUserDetails;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static com.healthcare.kb.constant.MessageConst.UNAUTHORIZED_TOKEN;


@Slf4j
@Service
public class KbhcSecurityService {

    private static final String ACCESS_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";
    private final Key secretKey;

    public KbhcSecurityService(@Value("${secret.key.jwt}") String tokenKey) {
        byte[] keyBites = Decoders.BASE64.decode(tokenKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBites);
    }

    @Transactional
    public JwtDto.Tokens getTokens(LoginUser.Info userInfo){

        String accessToken = this.createAccessToken(userInfo);
        String refreshToken = this.createRefreshToken(userInfo);

        return JwtDto.Tokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    public String createAccessToken(LoginUser.Info userInfo) {

        LocalDateTime now = LocalDateTime.now();
        Date accessTokenExpiresIn = Timestamp.valueOf(now.plusHours(12));

        Claims claims = Jwts.claims()
                .setSubject(String.valueOf(userInfo.getUserNo()));
        claims.put("userId", userInfo.getEmail());
        claims.put("name", userInfo.getName());

        String roleStr = String.join(",", userInfo.getRole().stream().map(RoleType::name).toList());
        claims.put("role", roleStr);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(accessTokenExpiresIn)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(LoginUser.Info userInfo){
        LocalDateTime now = LocalDateTime.now();
        Date refreshTokenExpiresIn = Timestamp.valueOf(now.plusDays(7));

        Claims claims = Jwts.claims()
                .setSubject(String.valueOf(userInfo.getUserNo()));

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(refreshTokenExpiresIn)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 헤더에서 AccessToken 추출 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서 헤더를 가져온 후 "Bearer"를
     * 삭제(""로 replace)
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(ACCESS_HEADER))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    public Optional<Long> extractUserNo(String refreshToken) {
        try {
            Claims body = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
            return Optional.of(Long.valueOf(String.valueOf(body.get("sub"))));
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    public Optional<UserDetails> getUserDetails(String accessToken) {
        try {

            Claims body = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            Long userNo = Long.valueOf(String.valueOf(body.get("sub")));
            String userId = String.valueOf(body.get("userId"));
            String name = String.valueOf(body.get("name"));
            String role = String.valueOf(body.get("role"));

            return Optional.ofNullable(AppUserDetails.valueOf(userNo, userId, name, role));
        } catch (Exception e) {
            log.error(UNAUTHORIZED_TOKEN.getMessage());
            return Optional.empty();
        }
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error(UNAUTHORIZED_TOKEN.getMessage()+" {}", e.getMessage());
            throw new AuthorizeException(UNAUTHORIZED_TOKEN);
        }
    }


}
