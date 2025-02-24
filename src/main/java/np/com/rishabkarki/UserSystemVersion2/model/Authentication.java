package np.com.rishabkarki.UserSystemVersion2.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import np.com.rishabkarki.UserSystemVersion2.util.SnowflakeIdGenerator;

@Entity
public class Authentication {
    @Id
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRoles roles;

    @Column(nullable = false)
    private boolean verified;

    public Authentication() {

    }

    public Authentication(String username, String email, String password, UserRoles roles) {
        SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1, 1);
        this.id = idGenerator.nextId();
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.verified = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRoles getRoles() {
        return roles;
    }

    public void setRoles(UserRoles roles) {
        this.roles = roles;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
