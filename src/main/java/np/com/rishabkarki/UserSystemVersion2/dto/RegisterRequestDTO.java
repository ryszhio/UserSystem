package np.com.rishabkarki.UserSystemVersion2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequestDTO(
        @NotBlank
        @Email
        String email,

        @Pattern(regexp = "^[a-zA-Z0-9._-]{3,20}$", message = "Username must be 3-20 character and can contain only letters, numbers, dots, underscores & hyphens.")
        String username,

        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\p{Punct}])[A-Za-z\\d\\p{Punct}]{8,}$",
                message = "Password must be at least 8 characters long, with 1 uppercase, 1 lowercase, 1 number, and 1 special character."
        )
        String password
) { }
