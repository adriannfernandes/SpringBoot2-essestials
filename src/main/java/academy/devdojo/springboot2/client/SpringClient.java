package academy.devdojo.springboot2.client;

import academy.devdojo.springboot2.domain.Livro;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Livro> entity = new RestTemplate().getForEntity("http://localhost:8080/livros/{id}", Livro.class, 1);
        log.info(entity);

        Livro object = new RestTemplate().getForObject("http://localhost:8080/livros/{id}", Livro.class, 1);
        log.info(object);

        Livro[] livros = new RestTemplate().getForObject("http://localhost:8080/livros/all", Livro[].class);
        log.info(Arrays.toString(livros));

        //@formatter:off
        ResponseEntity<List<Livro>> exchange = new RestTemplate().exchange("http://localhost:8080/livros/all",
                HttpMethod.GET,
               null,
                new ParameterizedTypeReference<>() {});
        //@formatter:on
        log.info(exchange.getBody());


        /*Livro kingdom = Livro.builder().name("Kingdom").build();
        Livro kingdomSaved = new RestTemplate().postForObject("http://localhost:8080/livros/", kingdom, Livro.class);
        log.info("saved livro {}", kingdomSaved);*/

        Livro magicodeoz = Livro.builder().name("Mágico de OZ").build();
        ResponseEntity<Livro> magicodeozSaved = new RestTemplate().exchange("http://localhost:8080/livros/",
                HttpMethod.POST,
                new HttpEntity<>(magicodeoz),
                Livro.class);
        log.info("saved livro {}", magicodeozSaved);
    }
}
