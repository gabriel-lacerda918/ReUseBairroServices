package br.com.reusebairro.controller;

import br.com.reusebairro.domain.Categoria;
import br.com.reusebairro.domain.StatusItem;
import br.com.reusebairro.dto.ColetaRequest;
import br.com.reusebairro.dto.ItemRequest;
import br.com.reusebairro.dto.ItemResponse;
import br.com.reusebairro.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/itens")
@Tag(name = "Itens", description = "Anúncios de itens para coleta")
public class ItemController {
    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Publicar item para coleta")
    public ItemResponse criar(@Valid @RequestBody ItemRequest request) {
        return service.criar(request);
    }

    @GetMapping
    @Operation(summary = "Listar itens", description = "Filtros opcionais: categoria, bairro (contém) e status")
    public List<ItemResponse> listar(@RequestParam(required = false) Categoria categoria,
                                     @RequestParam(required = false) String bairro,
                                     @RequestParam(required = false) StatusItem status) {
        return service.listar(categoria, bairro, status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar item por id")
    public ItemResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar item (somente se ainda disponível)")
    public ItemResponse atualizar(@PathVariable Long id, @Valid @RequestBody ItemRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover item")
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }

    @PatchMapping("/{id}/coleta")
    @Operation(summary = "Registrar coleta do item",
            description = "Marca o item como COLETADO. O anunciante não pode coletar o próprio item.")
    public ItemResponse coletar(@PathVariable Long id, @Valid @RequestBody ColetaRequest request) {
        return service.coletar(id, request.coletorId());
    }
}
