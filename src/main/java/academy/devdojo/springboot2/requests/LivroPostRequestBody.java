package academy.devdojo.springboot2.requests;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class LivroPostRequestBody {
    @NotEmpty(message = "The livro name cannot be empty")
    private String name;
}
