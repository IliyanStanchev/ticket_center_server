package tuvarna.si.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tuvarna.si.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
