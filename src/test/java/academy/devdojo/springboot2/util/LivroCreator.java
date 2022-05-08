package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.domain.Livro;

public class LivroCreator {

    public static Livro createLivroToBeSaved() {

        return Livro.builder()
                .name("Cidade de Papel")
                .build();
    }

    public static Livro createValidLivro(){

        return Livro.builder()
                .name("Cidade de Papel")
                .id((1L))
                .build();
    }

    public static Livro createValidUpdatedLivro(){

        return Livro.builder()
                .name("Cidade de Papel - Updated")
                .id(1L)
                .build();
    }
}
