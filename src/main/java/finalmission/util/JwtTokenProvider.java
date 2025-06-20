 package finalmission.util;

import finalmission.domain.vo.MemberRole;
import finalmission.dto.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;

    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SIG.HS256.key().build().getAlgorithm());
    }

    public String createToken(final Long memberId, final String name, final MemberRole role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .claim("memberId", memberId)
                .claim("name", name)
                .claim("role", role.toString())
                .issuedAt(now).expiration(validity)
                .signWith(secretKey).compact();
    }

    public TokenInfo getTokenInfo(final String token) {
        Claims claims = parseToken(token);
        return TokenInfo.from(claims);
    }

    private Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
