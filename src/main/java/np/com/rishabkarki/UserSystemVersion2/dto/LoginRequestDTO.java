package np.com.rishabkarki.UserSystemVersion2.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        String email,
        String username,
        @NotBlank(message = "Password field should not be blank.")
        String password
){ }