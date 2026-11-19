package br.com.reusebairro.controller;

import br.com.reusebairro.dto.UsuarioRequest;
import br.com.reusebairro.dto.UsuarioResponse;
import br.com.reusebairro.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Cadastro e consulta de usuários")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar usuário")
    public UsuarioResponse criar(@Valid @RequestBody UsuarioRequest request) {
        return service.criar(request);
    }

    @GetMapping
    @Operation(summary = "Listar usuários")
    public List<UsuarioResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por id")
    public UsuarioResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }
}
