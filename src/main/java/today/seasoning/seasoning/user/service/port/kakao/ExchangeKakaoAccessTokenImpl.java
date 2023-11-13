package today.seasoning.seasoning.user.service.port.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import today.seasoning.seasoning.common.exception.CustomException;

@Slf4j
@Component
public class ExchangeKakaoAccessTokenImpl implements ExchangeKakaoAccessToken {

	@Value("${KAKAO_REST_API_KEY}")
	private String restApiKey;

	@Value("${KAKAO_CLIENT_SECRET}")
	private String clientSecret;

	@Value("${KAKAO_LOGIN_REDIRECT_URL}")
	private String redirectURI;

	@Value("${KAKAO_LOGIN_ENDPOINT}")
	private String endPoint;

	@Override
	public String doExchange(String authorizationCode) {
		try {
			return exchangeAccessToken(authorizationCode);
		} catch (IOException e) {
			log.error("카카오 액세스 토큰 교환 오류");
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 로그인 실패");
		}
	}

	private String exchangeAccessToken(String authorizationCode) throws JsonProcessingException {
		HttpEntity<MultiValueMap<String, String>> httpEntity = createLoginHttpEntity(
			authorizationCode);
		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper objectMapper = new ObjectMapper();

		ResponseEntity<String> responseEntity = restTemplate.exchange(
			endPoint,
			HttpMethod.POST,
			httpEntity,
			String.class);

		if (responseEntity.getBody() == null || responseEntity.getBody().isBlank()) {
			return "";
		}

		return objectMapper.readTree(responseEntity.getBody())
			.get("access_token")
			.asText();
	}

	private HttpEntity<MultiValueMap<String, String>> createLoginHttpEntity(
		String authorizationCode) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", restApiKey);
		body.add("redirect_uri", redirectURI);
		body.add("code", authorizationCode);
		body.add("client_secret", clientSecret);

		return new HttpEntity<>(body, headers);
	}
}
