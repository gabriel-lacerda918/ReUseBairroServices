package br.com.reusebairro.dto;

import br.com.reusebairro.domain.Categoria;
import br.com.reusebairro.domain.Item;
import br.com.reusebairro.domain.StatusItem;
import java.time.LocalDateTime;

public record ItemResponse(Long id, String titulo, String descricao, Categoria categoria, String bairro,
                           String endereco, String cep, StatusItem status, Long anuncianteId,
                           String anuncianteNome, Long coletorId, LocalDateTime criadoEm,
                           LocalDateTime coletadoEm) {
    public static ItemResponse de(Item i) {
        return new ItemResponse(i.getId(), i.getTitulo(), i.getDescricao(), i.getCategoria(), i.getBairro(),
                i.getEndereco(), i.getCep(), i.getStatus(), i.getAnunciante().getId(),
                i.getAnunciante().getNome(), i.getColetor() == null ? null : i.getColetor().getId(),
                i.getCriadoEm(), i.getColetadoEm());
    }
}
