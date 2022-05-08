package academy.devdojo.springboot2.integration;

import academy.devdojo.springboot2.domain.Livro;
import academy.devdojo.springboot2.repository.LivroRepository;
import academy.devdojo.springboot2.util.LivroCreator;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
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

}
