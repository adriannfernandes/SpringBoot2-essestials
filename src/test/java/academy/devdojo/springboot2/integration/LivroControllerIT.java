package academy.devdojo.springboot2.integration;

import academy.devdojo.springboot2.domain.DevDojoUser;
import academy.devdojo.springboot2.domain.Livro;
import academy.devdojo.springboot2.repository.DevDojoUserRepository;
import academy.devdojo.springboot2.repository.LivroRepository;
import academy.devdojo.springboot2.requests.LivroPostRequestBody;
import academy.devdojo.springboot2.util.LivroCreator;
import academy.devdojo.springboot2.util.LivroPostRequestBodyCreator;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
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
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateRoleUser;
    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;
    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private DevDojoUserRepository devDojoUserRepository;

    private static final DevDojoUser USER = DevDojoUser.builder()
            .name("Devdojo Academy")
            .password("{bcrypt}$2a$10$4dT1yYNKB1k/cTdhyRJDa.mQQt5CAnF/WzbNiw1uUd5b2VOF/kg4m")
            .username("devdojo")
            .authorities("ROLE_USER")
            .build();
    private static final DevDojoUser ADMIN = DevDojoUser.builder()
            .name("Adrian Fernandes")
            .password("{bcrypt}$2a$10$4dT1yYNKB1k/cTdhyRJDa.mQQt5CAnF/WzbNiw1uUd5b2VOF/kg4m")
            .username("adrian")
            .authorities("ROLE_USER,ROLE_ADMIN")
            .build();

    @TestConfiguration
    @Lazy
    static class Config{
        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port){
            RestTemplateBuilder restTemplateBuilder =  new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("devdojo", "academy");
            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port){
            RestTemplateBuilder restTemplateBuilder =  new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("adrian", "academy");
            return new TestRestTemplate(restTemplateBuilder);
        }
    }
    @Test
    @DisplayName("List returns list of livros inside page object when successful ")
    void list_ReturnsListOfLivrosInsadePageObject_WhenSucessful(){
        Livro savedLivro = livroRepository.save(LivroCreator.createLivroToBeSaved());

        devDojoUserRepository.save(USER);

        String expectedName = savedLivro.getName();

        PageableResponse<Livro> livroPage = testRestTemplateRoleUser.exchange("/livros", HttpMethod.GET, null,
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

        devDojoUserRepository.save(USER);

        String expectedName = savedLivro.getName();

        List<Livro> livros = testRestTemplateRoleUser.exchange("/livros/all", HttpMethod.GET, null,
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

        devDojoUserRepository.save(USER);

        Long expectedId = savedLivro.getId();

        Livro livros = testRestTemplateRoleUser.getForObject("/livros/{id}", Livro.class, expectedId);

        Assertions.assertThat(livros).isNotNull();

        Assertions.assertThat(livros.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of livro when successful ")
    void findByName_ReturnsListOfLivro_WhenSucessful() {
            Livro savedLivro = livroRepository.save(LivroCreator.createLivroToBeSaved());

            devDojoUserRepository.save(USER);

             String expectedName = savedLivro.getName();

            String url = String.format("/livros/find?name=%s", expectedName);

            List<Livro> livros = testRestTemplateRoleUser.exchange(url, HttpMethod.GET, null,
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

        devDojoUserRepository.save(USER);

        List<Livro> livros = testRestTemplateRoleUser.exchange("/livros/find?name=xaxa", HttpMethod.GET, null,
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

        devDojoUserRepository.save(USER);

        ResponseEntity<Livro> livroResponseEntity = testRestTemplateRoleUser.postForEntity("/livros", livroPostRequestBody, Livro.class);

        Assertions.assertThat(livroResponseEntity).isNotNull();

        Assertions.assertThat(livroResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Assertions.assertThat(livroResponseEntity.getBody()).isNotNull();

        Assertions.assertThat(livroResponseEntity.getBody().getId()).isNotNull();

    }

    @Test
    @DisplayName("Replace updates livro when successful ")
    void replace_UpdatesLivro_WhenSucessful() {
        Livro savedLivro = livroRepository.save(LivroCreator.createLivroToBeSaved());

        devDojoUserRepository.save(USER);

        savedLivro.setName("new name");

        ResponseEntity<Void> livroResponseEntity = testRestTemplateRoleUser.exchange("/livros",
                HttpMethod.PUT, new HttpEntity<>(savedLivro), Void.class);

        Assertions.assertThat(livroResponseEntity).isNotNull();

        Assertions.assertThat(livroResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("Delete removes livro when successful ")
    void delete_RemovesLivro_WhenSucessful() {

        Livro savedLivro = livroRepository.save(LivroCreator.createLivroToBeSaved());

        devDojoUserRepository.save(ADMIN);

        ResponseEntity<Void> livroResponseEntity = testRestTemplateRoleAdmin.exchange("/livros/admin/{id}",
                HttpMethod.DELETE, null, Void.class, savedLivro.getId());

        Assertions.assertThat(livroResponseEntity).isNotNull();

        Assertions.assertThat(livroResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("Delete returns 403 when user is not ADMIN ")
    void delete_Returns403_WhenUserIsNotAdmin() {

        Livro savedLivro = livroRepository.save(LivroCreator.createLivroToBeSaved());

        devDojoUserRepository.save(USER);

        ResponseEntity<Void> livroResponseEntity = testRestTemplateRoleUser.exchange("/livros/admin/{id}",
                HttpMethod.DELETE, null, Void.class, savedLivro.getId());

        Assertions.assertThat(livroResponseEntity).isNotNull();

        Assertions.assertThat(livroResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }


}
