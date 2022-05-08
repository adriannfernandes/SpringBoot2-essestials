package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.domain.Livro;
import academy.devdojo.springboot2.requests.LivroPostRequestBody;

public class LivroPostRequestBodyCreator {

    public static LivroPostRequestBody createLivroPostRequestBody() {

        return LivroPostRequestBody.builder()
                .name(LivroCreator.createLivroToBeSaved().getName())
                .build();
    }
}
