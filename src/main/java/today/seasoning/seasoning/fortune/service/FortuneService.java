package today.seasoning.seasoning.fortune.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import today.seasoning.seasoning.fortune.domain.Fortune;
import today.seasoning.seasoning.fortune.domain.FortuneRepository;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class FortuneService {

    private final FortuneRepository fortuneRepository;

    // 운세 랜덤 조회
    public String findRandomFortune() {
        List<Fortune> all = fortuneRepository.findAll();
        Random random = new Random();
        Fortune fortune = all.get(random.nextInt(all.size()));
        return fortune.getContent();
    }
}
