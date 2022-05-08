package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Livro;
import academy.devdojo.springboot2.util.LivroCreator;
import ch.qos.logback.core.net.AbstractSSLSocketAppender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Test for Livro Repository")
class LivroRepositoryTest {

    @Autowired
    private LivroRepository livroRepository;

    @Test
    @DisplayName("Save persists livro when sucessful")
    void save_PersistLivro_WhenSucessful(){

        Livro livroToBeSaved = LivroCreator.createLivroToBeSaved();

        Livro livroSaved = this.livroRepository.save(livroToBeSaved);

        Assertions.assertThat(livroSaved).isNotNull();

        Assertions.assertThat(livroSaved.getId()).isNotNull();

        Assertions.assertThat(livroSaved.getName()).isEqualTo(livroToBeSaved.getName());

    }


    @Test
    @DisplayName("Save updates livro when sucessful")
    void save_UpdatesLivro_WhenSucessful(){

        Livro livroToBeSaved = LivroCreator.createLivroToBeSaved();

        Livro livroSaved = this.livroRepository.save(livroToBeSaved);

        livroSaved.setName("O Poder do hábito");

        Livro livroUpdated = this.livroRepository.save(livroSaved);

        Assertions.assertThat(livroUpdated).isNotNull();

        Assertions.assertThat(livroUpdated.getId()).isNotNull();

        Assertions.assertThat(livroUpdated.getName()).isEqualTo(livroSaved.getName());

    }

    @Test
    @DisplayName("Delete removes livro when sucessful")
    void delete_RemovesLivro_WhenSucessful(){

        Livro livroToBeSaved = LivroCreator.createLivroToBeSaved();

        Livro livroSaved = this.livroRepository.save(livroToBeSaved);

        this.livroRepository.delete(livroSaved);

        Optional<Livro> livroOptional = this.livroRepository.findById(livroSaved.getId());

        Assertions.assertThat(livroOptional).isEmpty();

    }

    @Test
    @DisplayName("Find By Name returns list of livro when sucessful")
    void findByName_ReturnsListOfLivro_WhenSucessful(){

        Livro livroToBeSaved = LivroCreator.createLivroToBeSaved();

        Livro livroSaved = this.livroRepository.save(livroToBeSaved);

        String name = livroSaved.getName();

        List<Livro> foundLivros = this.livroRepository.findByName(name);

        Assertions.assertThat(foundLivros)
                .isNotEmpty()
                .contains(livroSaved);


    }

    @Test
    @DisplayName("Find By Name returns empty list when no livro is found")
    void findByName_ReturnsEmptyList_WhenLivroIsNotFound(){

        List<Livro> foundLivros = this.livroRepository.findByName("xxxxxx");

        Assertions.assertThat(foundLivros).isEmpty();

    }
    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    void save_ThrowConstraintViolationException_WhenNameIsEmpty(){
        Livro livro = new Livro();
        Assertions.assertThatThrownBy(() -> this.livroRepository.save(livro))
                .isInstanceOf(ConstraintViolationException.class);
    }

}