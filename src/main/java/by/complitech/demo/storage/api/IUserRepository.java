package by.complitech.demo.storage.api;

import by.complitech.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
