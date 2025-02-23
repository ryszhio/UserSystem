package np.com.rishabkarki.UserSystemVersion2.service;

import np.com.rishabkarki.UserSystemVersion2.dto.LoginRequestDTO;
import np.com.rishabkarki.UserSystemVersion2.dto.RegisterRequestDTO;
import np.com.rishabkarki.UserSystemVersion2.model.Authentication;
import np.com.rishabkarki.UserSystemVersion2.model.UserRoles;
import np.com.rishabkarki.UserSystemVersion2.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationService {
    //dumb
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<Map<String, String>> register(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.existsByEmail(registerRequestDTO.email())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
               "message",
               "Email already exists."
            ));
        }

        if (userRepository.existsByUsername((registerRequestDTO.username()))) {
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

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Registered successfully !"));
    }

    public ResponseEntity<Map<String, String>> login(LoginRequestDTO loginRequestDTO) {
        Optional<Authentication> authenticationOptional;
        authenticationOptional = loginRequestDTO.email().isBlank() ?
                userRepository.findByUsername(loginRequestDTO.username()) : userRepository.findByEmail(loginRequestDTO.email());

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
