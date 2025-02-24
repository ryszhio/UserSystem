package np.com.rishabkarki.UserSystemVersion2.service;

import np.com.rishabkarki.UserSystemVersion2.dto.LoginRequestDTO;
import np.com.rishabkarki.UserSystemVersion2.dto.RegisterRequestDTO;
import np.com.rishabkarki.UserSystemVersion2.dto.VerifyRequestDTO;
import np.com.rishabkarki.UserSystemVersion2.model.Authentication;
import np.com.rishabkarki.UserSystemVersion2.model.UserRoles;
import np.com.rishabkarki.UserSystemVersion2.repository.AuthenticationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationRepository authenticationRepository;

    private final VerificationCodeService verificationService;

    public AuthenticationService(AuthenticationRepository authenticationRepository, PasswordEncoder passwordEncoder, VerificationCodeService verificationService) {
        this.authenticationRepository = authenticationRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationService = verificationService;
    }

    public ResponseEntity<Map<String, String>> register(RegisterRequestDTO registerRequestDTO) {
        if (authenticationRepository.existsByEmail(registerRequestDTO.email())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
               "message",
               "Email already exists."
            ));
        }

        if (authenticationRepository.existsByUsername((registerRequestDTO.username()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message",
                    "Username already exists."
            ));
        }

        Authentication user = new Authentication(
                registerRequestDTO.username(),
                registerRequestDTO.email(),
                registerRequestDTO.password(),
                UserRoles.USER
        );

        authenticationRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Registered successfully !"));
    }

    public ResponseEntity<Map<String, String>> login(LoginRequestDTO loginRequestDTO) {
        Optional<Authentication> authenticationOptional;
        authenticationOptional = loginRequestDTO.email().isBlank() ?
                authenticationRepository.findByUsername(loginRequestDTO.username()) : authenticationRepository.findByEmail(loginRequestDTO.email());

        if (authenticationOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message",
                    "Invalid username/email or password."
            ));
        }

        Authentication authentication = authenticationOptional.get();
        if (!passwordEncoder.matches(loginRequestDTO.password(), authentication.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message",
                    "Invalid username/email or password."
            ));
        }

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Logged in successfully !"));
    }
}
