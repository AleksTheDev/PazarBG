package bg.pazar.pazarbg.repo;

import bg.pazar.pazarbg.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
    UserEntity findByEmail(String email);
    List<UserEntity> findAllByIdNot(Long id);
}
