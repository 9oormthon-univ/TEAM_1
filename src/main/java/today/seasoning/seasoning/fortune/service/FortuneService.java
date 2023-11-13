package today.seasoning.seasoning.fortune.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import today.seasoning.seasoning.common.exception.CustomException;
import today.seasoning.seasoning.fortune.domain.FortuneRepository;

@Service
@RequiredArgsConstructor
public class FortuneService {

    private final FortuneRepository fortuneRepository;

    // 운세 랜덤 조회
    public String findRandomFortune() {
        return fortuneRepository.findByRandom()
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "운세 조회 실패"));
    }
}
