package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.DevDojoUser;
import academy.devdojo.springboot2.domain.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Classe Repository é a conexão direta com o BD (CHECK, SELECT, UPDATE...)

public interface DevDojoUserRepository extends JpaRepository<DevDojoUser, Long> {

    DevDojoUser findByUsername(String username);

}
