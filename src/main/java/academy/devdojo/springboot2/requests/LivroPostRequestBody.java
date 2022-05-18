package academy.devdojo.springboot2.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LivroPostRequestBody {
    @NotEmpty(message = "The livro name cannot be empty")
    @Schema(description = "This is the Livro's name", example = "OutSider", required = true)
    private String name;
}
