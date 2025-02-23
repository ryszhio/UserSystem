package np.com.rishabkarki.UserSystemVersion2.controller;

import np.com.rishabkarki.UserSystemVersion2.dto.LoginRequestDTO;
import np.com.rishabkarki.UserSystemVersion2.dto.RegisterRequestDTO;
import np.com.rishabkarki.UserSystemVersion2.service.AuthenticationService;
import np.com.rishabkarki.UserSystemVersion2.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    ResponseEntity<Map<String, String>> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return authenticationService.register(registerRequestDTO);
    }

    @PostMapping("/login")
    ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        return authenticationService.login(loginRequestDTO);
    }
}
