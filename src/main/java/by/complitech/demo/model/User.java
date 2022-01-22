package by.complitech.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fio", nullable = false)
    private String fullName;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    public User() {
    }

    public User(Long id, String fullName, String login, String email, String password) {
        this.id = id;
        this.fullName = fullName;
        this.login = login;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && fullName.equals(user.fullName) && login.equals(user.login) && email.equals(user.email) && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, login, email, password);
    }
}
