package today.seasoning.seasoning.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import today.seasoning.seasoning.common.exception.CustomException;

@Slf4j
public class EntitySerializationUtil {

	private final static ObjectMapper objectMapper = new ObjectMapper();

	public static String serialize(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error("JSON 직렬화 오류 : {}", e.getMessage());
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 오류 발생");
		}
	}
}
