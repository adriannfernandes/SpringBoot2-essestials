package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Livro;
import academy.devdojo.springboot2.service.LivroService;
import academy.devdojo.springboot2.util.LivroCreator;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

}