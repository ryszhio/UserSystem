package np.com.rishabkarki.UserSystemVersion2.service;

import np.com.rishabkarki.UserSystemVersion2.dto.VerifyRequestDTO;
import np.com.rishabkarki.UserSystemVersion2.model.Authentication;
import np.com.rishabkarki.UserSystemVersion2.model.TokenType;
import np.com.rishabkarki.UserSystemVersion2.model.VerificationCode;
import np.com.rishabkarki.UserSystemVersion2.repository.AuthenticationRepository;
import np.com.rishabkarki.UserSystemVersion2.repository.VerificationCodeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;

    private final AuthenticationRepository authenticationRepository;

    private final EmailService emailService;

    public VerificationCodeService(VerificationCodeRepository verificationCodeRepository, AuthenticationRepository authenticationRepository, EmailService emailService) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.authenticationRepository = authenticationRepository;
        this.emailService = emailService;
    }

    private final Random random = new Random();

    public ResponseEntity<Map<String, String>> generate(String email, TokenType tokenType) {
        VerificationCode verificationCode = new VerificationCode(
                email,
                random.nextInt(900000) + 100000,
                LocalDateTime.now().plusMinutes(10),
                tokenType,
                false
        );

        emailService.sendVerificationEmail(email, verificationCode.getToken());
        verificationCodeRepository.save(verificationCode);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "message",
                "A mail have been sent containing verification code."));
    }

    public ResponseEntity<Map<String, String>> validate(VerifyRequestDTO verifyRequestDTO) {
        Optional<VerificationCode> codeOptional = verificationCodeRepository.findByEmailAndToken(verifyRequestDTO.email(), verifyRequestDTO.token());

        if (codeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message",
                    "Invalid verification code."
            ));
        }

        VerificationCode code = codeOptional.get();

        if (code.isExpired()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message",
                    "Invalid verification code. Might have expired !"
            ));
        }

        code.setExpired(true);
        verificationCodeRepository.save(code);

        updateUserVerification(verifyRequestDTO.email());

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "message",
                "Your email is now verified !"
        ));
    }

    private void updateUserVerification(String email) {
        Optional<Authentication> optionalAuthentication = authenticationRepository.findByEmail(email);

        if (optionalAuthentication.isEmpty()) {
            throw new RuntimeException("No user was found with that email !");
        }

        Authentication authentication = optionalAuthentication.get();

        authentication.setVerified(true);
        authenticationRepository.save(authentication);
    }
}