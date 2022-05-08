package academy.devdojo.springboot2.integration;

import academy.devdojo.springboot2.domain.Livro;
import academy.devdojo.springboot2.repository.LivroRepository;
import academy.devdojo.springboot2.requests.LivroPostRequestBody;
import academy.devdojo.springboot2.util.LivroCreator;
import academy.devdojo.springboot2.util.LivroPostRequestBodyCreator;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LivroControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;
    @Autowired
    private LivroRepository livroRepository;
    @Test
    @DisplayName("List returns list of livros inside page object when successful ")
    void list_ReturnsListOfLivrosInsadePageObject_WhenSucessful(){
        Livro savedLivro = livroRepository.save(LivroCreator.createLivroToBeSaved());

        String expectedName = savedLivro.getName();

        PageableResponse<Livro> livroPage = testRestTemplate.exchange("/livros", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Livro>>() {
                }).getBody();

        Assertions.assertThat(livroPage).isNotNull();

        Assertions.assertThat(livroPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(livroPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("ListAll returns list of livros when successful ")
    void list_ReturnsListOfLivros_WhenSucessful(){
        Livro savedLivro = livroRepository.save(LivroCreator.createLivroToBeSaved());

        String expectedName = savedLivro.getName();

        List<Livro> livros = testRestTemplate.exchange("/livros/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Livro>>() {
                }).getBody();

        Assertions.assertThat(livros)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(livros.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns livro when successful ")
    void findById_ReturnsLivro_WhenSucessful() {
        Livro savedLivro = livroRepository.save(LivroCreator.createLivroToBeSaved());

        Long expectedId = savedLivro.getId();

        Livro livros = testRestTemplate.getForObject("/livros/{id}", Livro.class, expectedId);

        Assertions.assertThat(livros).isNotNull();

        Assertions.assertThat(livros.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of livro when successful ")
    void findByName_ReturnsListOfLivro_WhenSucessful() {
            Livro savedLivro = livroRepository.save(LivroCreator.createLivroToBeSaved());

            String expectedName = savedLivro.getName();

            String url = String.format("/livros/find?name=%s", expectedName);

            List<Livro> livros = testRestTemplate.exchange(url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Livro>>() {
                    }).getBody();

            Assertions.assertThat(livros)
                    .isNotNull()
                    .isNotEmpty()
                    .hasSize(1);

            Assertions.assertThat(livros.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of livro when livro is not found")
    void findByName_ReturnsAnEmptyListOfLivro_WhenLivroIsNotFound() {

        List<Livro> livros = testRestTemplate.exchange("/livros/find?name=xaxa", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Livro>>() {
                }).getBody();

        Assertions.assertThat(livros)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("Save returns livro when successful ")
    void save_ReturnsLivro_WhenSucessful() {
        LivroPostRequestBody livroPostRequestBody = LivroPostRequestBodyCreator.createLivroPostRequestBody();

        ResponseEntity<Livro> livroResponseEntity = testRestTemplate.postForEntity("/livros", livroPostRequestBody, Livro.class);

        Assertions.assertThat(livroResponseEntity).isNotNull();

        Assertions.assertThat(livroResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Assertions.assertThat(livroResponseEntity.getBody()).isNotNull();

        Assertions.assertThat(livroResponseEntity.getBody().getId()).isNotNull();

    }

    @Test
    @DisplayName("Replace updates livro when successful ")
    void replace_UpdatesLivro_WhenSucessful() {
        Livro savedLivro = livroRepository.save(LivroCreator.createLivroToBeSaved());

        savedLivro.setName("new name");

        ResponseEntity<Void> livroResponseEntity = testRestTemplate.exchange("/livros",
                HttpMethod.PUT, new HttpEntity<>(savedLivro), Void.class);

        Assertions.assertThat(livroResponseEntity).isNotNull();

        Assertions.assertThat(livroResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("Delete removes livro when successful ")
    void delete_RemovesLivro_WhenSucessful() {

        Livro savedLivro = livroRepository.save(LivroCreator.createLivroToBeSaved());

        ResponseEntity<Void> livroResponseEntity = testRestTemplate.exchange("/livros/{id}",
                HttpMethod.DELETE, null, Void.class, savedLivro.getId());

        Assertions.assertThat(livroResponseEntity).isNotNull();

        Assertions.assertThat(livroResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }


}
