package np.com.rishabkarki.UserSystemVersion2.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import np.com.rishabkarki.UserSystemVersion2.model.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenGenerator {
    @Value("${jwt.secret.key}")
    private String secretKey;

    public String generateToken(Long id, String username, Boolean isVerified) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("isVerified", isVerified.toString());
        claims.put("username", username);
        claims.put("id", id);
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 864000L))
                .signWith(getSignKey())
                .compact();
    }

    public Long extractId(String token) {
        return Long.valueOf(extractAllClaims(token).get("id").toString());
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).get("username").toString();
    }

    public Boolean extractIsVerified(String token) {
        return Boolean.valueOf(extractAllClaims(token).get("isVerified").toString());
    }

    public Boolean validateToken(String token) {
        final String username = extractUsername(token);
        final Long id = extractId(token);

        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
