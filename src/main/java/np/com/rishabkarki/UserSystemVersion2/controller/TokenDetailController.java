package np.com.rishabkarki.UserSystemVersion2.controller;

import np.com.rishabkarki.UserSystemVersion2.util.JwtTokenGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/token")
public class TokenDetailController {

    private final JwtTokenGenerator jwtTokenGenerator;

    public TokenDetailController(JwtTokenGenerator jwtTokenGenerator) {
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    @PostMapping("/getId")
    public ResponseEntity<Map<String, Long>> getUserId(@RequestBody Map<String, String> requestBody){
        String token = requestBody.get("token");

        System.out.println(token);
        Long id = jwtTokenGenerator.extractId(token);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "id",
                id
        ));
    }

    @PostMapping("/getUsername")
    public ResponseEntity<Map<String, String>> getUsername(@RequestBody Map<String, String> requestBody) {
        String token = requestBody.get("token");

        String username = jwtTokenGenerator.extractUsername(token);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "username",
                username
        ));
    }

    @PostMapping("/getIsVerified")
    public ResponseEntity<Map<String, String>> getIsVerified(@RequestBody Map<String, String> requestBody) {
        String token = requestBody.get("token");

        Boolean isVerified = jwtTokenGenerator.extractIsVerified(token);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "isVerified",
                isVerified.toString()
        ));
    }
}