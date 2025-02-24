package np.com.rishabkarki.UserSystemVersion2.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    private Integer token;

    private LocalDateTime expiryDate;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(nullable = false)
    private Boolean isExpired;

    public VerificationCode() {

    }

    public VerificationCode(String email, Integer token, LocalDateTime expiryDate, TokenType tokenType, boolean isExpired) {
        this.email = email;
        this.token = token;
        this.expiryDate = expiryDate;
        this.tokenType = tokenType;
        this.isExpired = isExpired;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }

    public LocalDateTime getExpiry() {
        return expiryDate;
    }

    public void setExpiry(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public boolean isExpired() {
        if (LocalDateTime.now().isAfter(expiryDate))
            setExpired(true);

        return isExpired;
    }

    public void setExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }
}
