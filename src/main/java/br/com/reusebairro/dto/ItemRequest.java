package br.com.reusebairro.dto;

import br.com.reusebairro.domain.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ItemRequest(
        @NotBlank @Size(max = 120) String titulo,
        @Size(max = 500) String descricao,
        @NotNull Categoria categoria,
        @NotBlank @Size(max = 80) String bairro,
        @NotBlank @Size(max = 200) String endereco,
        @Pattern(regexp = "^\\d{8}$", message = "CEP deve ter 8 dígitos") String cep,
        @NotNull Long anuncianteId) {
}
