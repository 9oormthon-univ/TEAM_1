package today.seasoning.seasoning.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import today.seasoning.seasoning.common.enums.LoginType;

@Component
public class JwtUtil {

	@Value("${JWT_SECRET_KEY}")
	private String jwtSecretKey;

	@Value("${JWT_EXPIRATION_TIME}")
	private Long expirationTime;

	public String createToken(Long id, LoginType loginType) {
		Map<String, String> claims = new HashMap<>();
		claims.put("id", String.valueOf(id));
		claims.put("loginType", loginType.name());

		SecretKey secretKey = createSecretKey();

		return Jwts.builder()
			.claims(claims)
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + expirationTime))
			.signWith(secretKey)
			.compact();
	}

	public Long getUserId(String token) {
		return Long.parseLong(getClaims(token).get("id", String.class));
	}

	public LoginType getLoginType(String token) {
		return LoginType.valueOf(getClaims(token).get("loginType", String.class));
	}

	private Claims getClaims(String token) {
		return Jwts.parser().verifyWith(createSecretKey()).build().parseSignedClaims(token)
			.getPayload();
	}

	private SecretKey createSecretKey() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}