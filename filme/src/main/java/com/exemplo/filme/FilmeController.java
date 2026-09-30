package com.exemplo.filme;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/filmes")
public class FilmeController {
    private final List<Filme> filmes = new ArrayList<>(List.of(
            new Filme(1L, "O Poderoso Chefao", "Drama", 1972),
            new Filme(2L, "Interestelar", "Ficcao cientifica", 2014),
            new Filme(3L, "Parasita", "Suspense", 2019)
    ));
    private final AtomicLong proximoId = new AtomicLong(4);

    @GetMapping
    public List<Filme> listar() {
        return filmes;
    }

    @GetMapping("/{id}")
    public Filme buscarPorId(@PathVariable Long id) {
        return encontrar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Filme cadastrar(@RequestBody Filme filme) {
        filme.setId(proximoId.getAndIncrement());
        filmes.add(filme);
        return filme;
    }

    @PutMapping("/{id}")
    public Filme atualizar(@PathVariable Long id, @RequestBody Filme dados) {
        Filme filme = encontrar(id);
        filme.setTitulo(dados.getTitulo());
        filme.setGenero(dados.getGenero());
        filme.setAnoLancamento(dados.getAnoLancamento());
        return filme;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        filmes.remove(encontrar(id));
    }

    private Filme encontrar(Long id) {
        return filmes.stream()
                .filter(filme -> filme.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Filme nao encontrado"));
    }
}
