package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Livro;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Test for Livro Repository")
class LivroRepositoryTest {

    @Autowired
    private LivroRepository livroRepository;

    @Test
    @DisplayName("Save creates livro when sucessful")
    void save_PersistLivro_WhenSucessful(){

        Livro livroToBeSaved = createLivro();
        Livro livroSaved = this.livroRepository.save(livroToBeSaved);

        Assertions.assertThat(livroSaved).isNotNull();
        Assertions.assertThat(livroSaved.getId()).isNotNull();
        Assertions.assertThat(livroSaved.getName()).isEqualTo(livroToBeSaved.getName());

    }

    private Livro createLivro(){

        return Livro.builder()
                .name("Cidade de Papel")
                .build();
    }
}