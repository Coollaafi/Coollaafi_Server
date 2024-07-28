package coollaafi.wot.jwt;

import coollaafi.wot.apiPayload.exception.GeneralException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Key key;

    public JwtTokenProvider(@Value("wVtm60YHDIV4GbVtxK3rE59xDZqLLtsjT4nIlPwvW1p3vIjN2jHFZ3VcZ7vTXnZqjF8hJf9vJ8Jf9jF8hJf9J8Jf9vJ8Jf9jF") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String accessTokenGenerate(String subject, Date expiredAt) {
        return Jwts.builder()
                .setSubject(subject)	//uid
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
    public String refreshTokenGenerate(Date expiredAt) {
        return Jwts.builder()
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new GeneralException("The token has expired.");
        } catch (JwtException | IllegalArgumentException e) {
            throw new GeneralException("The token is not valid.");
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        String username = claims.getSubject();

        if (username == null) {
            throw new GeneralException("The token is not valid.");
        }

        UserDetails userDetails = User.builder()
                .username(username)
                .password("") // password는 필요없으므로 빈 문자열
                .authorities(List.of()) // authorities를 설정할 경우 추가
                .build();

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public static void checkAuthorizationHeader(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new GeneralException("Token not delivered."); // 토큰 전달되지 않았을 때 메시지
        }
    }
}
