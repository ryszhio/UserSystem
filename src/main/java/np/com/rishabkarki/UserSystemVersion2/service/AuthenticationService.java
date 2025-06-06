package np.com.rishabkarki.UserSystemVersion2.service;

import jakarta.servlet.http.HttpServletResponse;
import np.com.rishabkarki.UserSystemVersion2.dto.LoginRequestDTO;
import np.com.rishabkarki.UserSystemVersion2.dto.RegisterRequestDTO;
import np.com.rishabkarki.UserSystemVersion2.model.Authentication;
import np.com.rishabkarki.UserSystemVersion2.model.TokenType;
import np.com.rishabkarki.UserSystemVersion2.model.UserRoles;
import np.com.rishabkarki.UserSystemVersion2.repository.AuthenticationRepository;
import np.com.rishabkarki.UserSystemVersion2.util.JwtTokenGenerator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationRepository authenticationRepository;

    private final JwtTokenGenerator jwtTokenGenerator;

    private final VerificationCodeService verificationCodeService;

    public AuthenticationService(AuthenticationRepository authenticationRepository, PasswordEncoder passwordEncoder, JwtTokenGenerator jwtTokenGenerator, VerificationCodeService verificationCodeService) {
        this.authenticationRepository = authenticationRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.verificationCodeService = verificationCodeService;
    }

    @Transactional
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

        String hashedPassword = passwordEncoder.encode(registerRequestDTO.password());

        Authentication user = new Authentication(
                registerRequestDTO.username(),
                registerRequestDTO.email(),
                hashedPassword,
                UserRoles.USER
        );

        authenticationRepository.save(user);

        return verificationCodeService.generate(user.getEmail(), TokenType.EMAIL_VERIFICATION);
    }

    @Transactional
    public ResponseEntity<Map<String, String>> login(LoginRequestDTO loginRequestDTO, HttpServletResponse servletResponse) {
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

        if (!authentication.isVerified()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "message",
                    "Verify your email address."
            ));
        }

        String token = jwtTokenGenerator.generateToken(authentication.getId(), authentication.getUsername(), authentication.isVerified());

        ResponseCookie cookie = ResponseCookie.from("usr_r", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(3600 * 24)
                .path("/")
                .build();

        servletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Logged in successfully !"));
    }
    
    @Transactional
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse servletResponse) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(0)
                .path("/")
                .build();

        servletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "message",
                "Successfully logged out!"
        ));
    }
}
