package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Livro;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.repository.LivroRepository;
import academy.devdojo.springboot2.util.LivroCreator;
import academy.devdojo.springboot2.util.LivroPostRequestBodyCreator;
import academy.devdojo.springboot2.util.LivroPutRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class LivroServiceTest {

    @InjectMocks
    private LivroService livroService;
    @Mock
    private LivroRepository livroRepositoryMock;

    @BeforeEach
    void setUp(){
        PageImpl<Livro> livroPage = new PageImpl<>(List.of(LivroCreator.createValidLivro()));

        BDDMockito.when(livroRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(livroPage);

        BDDMockito.when(livroRepositoryMock.findAll())
                .thenReturn(List.of(LivroCreator.createValidLivro()));

        BDDMockito.when(livroRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(LivroCreator.createValidLivro()));

        BDDMockito.when(livroRepositoryMock.findByName(ArgumentMatchers.any()))
                .thenReturn(List.of(LivroCreator.createValidLivro()));

        BDDMockito.when(livroRepositoryMock.save(ArgumentMatchers.any(Livro.class)))
                .thenReturn(LivroCreator.createValidLivro());


        BDDMockito.doNothing().when(livroRepositoryMock).delete(ArgumentMatchers.any(Livro.class));



    }
    @Test
    @DisplayName("List returns list of All livros inside page object when successful ")
    void listAll_ReturnsListOfLivrosInsadePageObject_WhenSucessful(){
        String expectedName = LivroCreator.createValidLivro().getName();

        Page<Livro> livroPage = livroService.listAll(PageRequest.of(1,1));

        Assertions.assertThat(livroPage).isNotNull();

        Assertions.assertThat(livroPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(livroPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAllNonPageable returns list of livros when successful ")
    void listAllNonPageable_ReturnsListOfLivros_WhenSucessful(){
        String expectedName = LivroCreator.createValidLivro().getName();

        List<Livro> livros = livroService.listAllNonPageable();

        Assertions.assertThat(livros)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(livros.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns livro when successful ")
    void findByIdOrThrowBadRequestException_ReturnsLivro_WhenSucessful() {
        Long expectedId = LivroCreator.createValidLivro().getId();

        Livro livros = livroService.findByIdOrThrowBadRequestException(1);

        Assertions.assertThat(livros).isNotNull();

        Assertions.assertThat(livros.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when livro is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenLivroIsNotFound() {

        BDDMockito.when(livroRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> livroService.findByIdOrThrowBadRequestException(1));

    }

    @Test
    @DisplayName("findByName returns list of livro when successful ")
    void findByName_ReturnsListOfLivro_WhenSucessful() {
        String expectedName = LivroCreator.createValidLivro().getName();

        List<Livro> livros = livroService.findByName("livro");

        Assertions.assertThat(livros)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(livros.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of livro when livro is not found")
    void findByName_ReturnsAnEmptyListOfLivro_WhenLivroIsNotFound() {
        BDDMockito.when(livroRepositoryMock.findByName(ArgumentMatchers.any()))
                .thenReturn(Collections.emptyList());

        List<Livro> livros = livroService.findByName("livro");

        Assertions.assertThat(livros)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("Save returns livro when successful ")
    void save_ReturnsLivro_WhenSucessful() {

        Livro livros = livroService.save(LivroPostRequestBodyCreator.createLivroPostRequestBody());

        Assertions.assertThat(livros).isNotNull().isEqualTo(LivroCreator.createValidLivro());

    }

    @Test
    @DisplayName("Replace updates livro when successful ")
    void replace_UpdatesLivro_WhenSucessful() {

        Assertions.assertThatCode(() -> livroService.replace(LivroPutRequestBodyCreator.createLivroPutRequestBody()))
                .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("Delete removes livro when successful ")
    void delete_RemovesLivro_WhenSucessful() {

        Assertions.assertThatCode(() -> livroService.delete(1))
                .doesNotThrowAnyException();

    }

}