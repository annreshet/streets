package streets.common.entities;

import streets.common.wrappers.UserWrapper;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", length = 30, nullable = false)
    private String username;

    @Column(name = "password", length = 30, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", length = 30, nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Street street;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UserWrapper getWrapper() {
        return new UserWrapper(id, username, password, role, street);
    }
}