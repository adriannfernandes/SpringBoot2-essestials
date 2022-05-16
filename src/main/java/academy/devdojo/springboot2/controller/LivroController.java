package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Livro;
import academy.devdojo.springboot2.requests.LivroPostRequestBody;
import academy.devdojo.springboot2.requests.LivroPutRequestBody;
import academy.devdojo.springboot2.service.LivroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
@Log4j2

// Classe Controller é aonde vai estar os EndPoints
public class LivroController {

    private final LivroService livroService;

    @GetMapping
    public ResponseEntity<Page<Livro>> list(Pageable pageable){

        return ResponseEntity.ok(livroService.listAll(pageable));
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Livro>> listAll(){
        return ResponseEntity.ok(livroService.listAllNonPageable());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Livro> findById(@PathVariable long id){
        return ResponseEntity.ok(livroService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "by-id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Livro> findByIdAuthenticationPrincipal(@PathVariable long id,
                                                                 @AuthenticationPrincipal UserDetails userDetails){
        log.info(userDetails);
        return ResponseEntity.ok(livroService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Livro>> findByName(@RequestParam String name){
        return ResponseEntity.ok(livroService.findByName(name));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Livro> save(@RequestBody @Valid LivroPostRequestBody livroPostRequestBody){
        return new ResponseEntity<Livro>(livroService.save(livroPostRequestBody), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Livro> delete(@PathVariable long id){
        livroService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Livro> replace(@RequestBody LivroPutRequestBody livroPutRequestBody){
        livroService.replace(livroPutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
