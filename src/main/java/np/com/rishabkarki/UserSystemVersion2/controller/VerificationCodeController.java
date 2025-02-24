package np.com.rishabkarki.UserSystemVersion2.controller;

import np.com.rishabkarki.UserSystemVersion2.dto.VerifyRequestDTO;
import np.com.rishabkarki.UserSystemVersion2.model.TokenType;
import np.com.rishabkarki.UserSystemVersion2.service.VerificationCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/verification")
public class VerificationCodeController {
    private final VerificationCodeService verificationService;

    public VerificationCodeController(VerificationCodeService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateCode(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        return verificationService.generate(email, TokenType.EMAIL_VERIFICATION);
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, String>> validateCode(@RequestBody VerifyRequestDTO verifyRequestDTO) {
        return verificationService.validate(verifyRequestDTO);
    }
}
