package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Livro;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.mapper.LivroMapper;
import academy.devdojo.springboot2.repository.LivroRepository;
import academy.devdojo.springboot2.requests.LivroPostRequestBody;
import academy.devdojo.springboot2.requests.LivroPutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
//Class Service é onde vai estar toda REGRA DE NEGÓCIO
public class LivroService {


    private final LivroRepository livroRepository;

    public Page<Livro> listAll(Pageable pageable){
        return livroRepository.findAll(pageable);
    }

    public List<Livro> listAllNonPageable() {
        return livroRepository.findAll();
    }
    public List<Livro> findByName(String name){
        return livroRepository.findByName(name);
    }

    public Livro findByIdOrThrowBadRequestException(long id){
        return livroRepository.findById(id)
                .orElseThrow(()-> new BadRequestException("Livro Not Found"));
    }

    @Transactional
    public Livro save(LivroPostRequestBody livroPostRequestBody) {
        return livroRepository.save(LivroMapper.INSTANCE.toLivro(livroPostRequestBody));
    }

    public void delete(long id) {
        livroRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(LivroPutRequestBody livroPutRequestBody) {
        Livro savedLivro = findByIdOrThrowBadRequestException(livroPutRequestBody.getId());
        Livro livro = LivroMapper.INSTANCE.toLivro(livroPutRequestBody);
        livro.setId(savedLivro.getId());
        livroRepository.save(livro);
    }
}
