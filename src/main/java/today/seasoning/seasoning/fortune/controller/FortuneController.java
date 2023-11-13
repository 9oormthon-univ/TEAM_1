package today.seasoning.seasoning.fortune.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import today.seasoning.seasoning.fortune.service.FortuneService;

@RestController
@RequiredArgsConstructor
public class FortuneController {

    private final FortuneService fortuneService;

    @GetMapping("/today-fortune")
    public ResponseEntity<String> findRandomFortune() {
        String fortuneContent = fortuneService.findRandomFortune();
        return ResponseEntity.ok().body(fortuneContent);
    }
}
