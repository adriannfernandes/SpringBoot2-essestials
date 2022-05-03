package academy.devdojo.springboot2.requests;

import lombok.Data;

@Data
public class LivroPutRequestBody {
    private Long id;
    private String name;
}
