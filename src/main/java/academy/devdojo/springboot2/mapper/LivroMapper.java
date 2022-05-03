package academy.devdojo.springboot2.mapper;

import academy.devdojo.springboot2.domain.Livro;
import academy.devdojo.springboot2.requests.LivroPostRequestBody;
import academy.devdojo.springboot2.requests.LivroPutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class LivroMapper {

    public static final LivroMapper INSTANCE = Mappers.getMapper(LivroMapper.class);

    public abstract Livro toLivro(LivroPostRequestBody livroPostRequestBody);

    public abstract Livro toLivro(LivroPutRequestBody livroPutRequestBody);



}
