package today.seasoning.seasoning.common.config;

import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import today.seasoning.seasoning.common.UserPrincipal;
import today.seasoning.seasoning.common.exception.CustomException;
import today.seasoning.seasoning.common.util.JwtUtil;
import today.seasoning.seasoning.user.domain.User;
import today.seasoning.seasoning.user.domain.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws IOException, ServletException {

		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰 누락");
			return;
		}

		String token = authorization.split(" ")[1];

		try {
			UserPrincipal userPrincipal = createPrincipalFromToken(token);

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				userPrincipal, null, List.of(new SimpleGrantedAuthority("USER")));

			authenticationToken.setDetails(
				new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		} catch (ExpiredJwtException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰 만료");
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "토큰 오류");
			return;
		}
		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		List<String> excludePath = Arrays.asList("/oauth/kakao/login");

		String path = request.getRequestURI();

		return excludePath.stream().anyMatch(path::equals);
	}

	private UserPrincipal createPrincipalFromToken(String token) {
		User user = userRepository.findById(jwtUtil.getUserId(token))
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "회원 조회 실패"));

		return UserPrincipal.builder(user);
	}
}