package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Livro;
import academy.devdojo.springboot2.requests.LivroPostRequestBody;
import academy.devdojo.springboot2.requests.LivroPutRequestBody;
import academy.devdojo.springboot2.service.LivroService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
class LivroControllerTest {

    @InjectMocks
    private LivroController livroController;
    @Mock
    private LivroService livroServiceMock;

    @BeforeEach
    void setUp(){
        PageImpl<Livro> livroPage = new PageImpl<>(List.of(LivroCreator.createValidLivro()));

        BDDMockito.when(livroServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(livroPage);

        BDDMockito.when(livroServiceMock.listAllNonPageable())
                .thenReturn(List.of(LivroCreator.createValidLivro()));

        BDDMockito.when(livroServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(LivroCreator.createValidLivro());

        BDDMockito.when(livroServiceMock.findByName(ArgumentMatchers.any()))
                .thenReturn(List.of(LivroCreator.createValidLivro()));

        BDDMockito.when(livroServiceMock.save(ArgumentMatchers.any(LivroPostRequestBody.class)))
                .thenReturn(LivroCreator.createValidLivro());

        BDDMockito.doNothing().when(livroServiceMock).replace(ArgumentMatchers.any(LivroPutRequestBody.class));

        BDDMockito.doNothing().when(livroServiceMock).delete(ArgumentMatchers.anyLong());



    }
    @Test
    @DisplayName("List returns list of livros inside page object when successful ")
    void list_ReturnsListOfLivrosInsadePageObject_WhenSucessful(){
        String expectedName = LivroCreator.createValidLivro().getName();

        Page<Livro> livroPage = livroController.list(null).getBody();

        Assertions.assertThat(livroPage).isNotNull();

        Assertions.assertThat(livroPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(livroPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("ListAll returns list of livros when successful ")
    void list_ReturnsListOfLivros_WhenSucessful(){
        String expectedName = LivroCreator.createValidLivro().getName();

       List<Livro> livros = livroController.listAll().getBody();

        Assertions.assertThat(livros)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(livros.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns livro when successful ")
    void findById_ReturnsLivro_WhenSucessful() {
        Long expectedId = LivroCreator.createValidLivro().getId();

        Livro livros = livroController.findById(1).getBody();

        Assertions.assertThat(livros).isNotNull();

        Assertions.assertThat(livros.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of livro when successful ")
    void findByName_ReturnsListOfLivro_WhenSucessful() {
        String expectedName = LivroCreator.createValidLivro().getName();

        List<Livro> livros = livroController.findByName("livro").getBody();

        Assertions.assertThat(livros)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(livros.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of livro when livro is not found")
    void findByName_ReturnsAnEmptyListOfLivro_WhenLivroIsNotFound() {
        BDDMockito.when(livroServiceMock.findByName(ArgumentMatchers.any()))
                .thenReturn(Collections.emptyList());

        List<Livro> livros = livroController.findByName("livro").getBody();

        Assertions.assertThat(livros)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("Save returns livro when successful ")
    void save_ReturnsLivro_WhenSucessful() {

        Livro livros = livroController.save(LivroPostRequestBodyCreator.createLivroPostRequestBody()).getBody();

        Assertions.assertThat(livros).isNotNull().isEqualTo(LivroCreator.createValidLivro());

    }

    @Test
    @DisplayName("Replace updates livro when successful ")
    void replace_UpdatesLivro_WhenSucessful() {

        Assertions.assertThatCode(() -> livroController.replace(LivroPutRequestBodyCreator.createLivroPutRequestBody()))
                .doesNotThrowAnyException();

        ResponseEntity<Livro> entity = livroController.replace(LivroPutRequestBodyCreator.createLivroPutRequestBody());

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);



    }

    @Test
    @DisplayName("Delete removes livro when successful ")
    void delete_RemovesLivro_WhenSucessful() {

        Assertions.assertThatCode(() -> livroController.delete(1))
                .doesNotThrowAnyException();

        ResponseEntity<Livro> entity = livroController.delete(1);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

}