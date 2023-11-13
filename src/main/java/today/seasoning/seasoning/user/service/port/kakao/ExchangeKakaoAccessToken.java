package today.seasoning.seasoning.user.service.port.kakao;

public interface ExchangeKakaoAccessToken {

	String doExchange(String authorizationCode);
}
