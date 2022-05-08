package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.requests.LivroPutRequestBody;

public class LivroPutRequestBodyCreator {

    public static LivroPutRequestBody createLivroPutRequestBody() {

        return LivroPutRequestBody.builder()
                .id(LivroCreator.createValidUpdatedLivro().getId())
                .name(LivroCreator.createValidUpdatedLivro().getName())
                .build();
    }
}
