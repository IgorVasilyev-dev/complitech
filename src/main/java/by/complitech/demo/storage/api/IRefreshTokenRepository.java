package by.complitech.demo.storage.api;

import by.complitech.demo.model.RefreshToken;
import by.complitech.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, User> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUserId(Long id);

}
