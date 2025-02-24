package np.com.rishabkarki.UserSystemVersion2.dto;

import jakarta.validation.constraints.NotBlank;

public record VerifyRequestDTO(
        @NotBlank
        String email,
        @NotBlank
        Integer token
) { }
