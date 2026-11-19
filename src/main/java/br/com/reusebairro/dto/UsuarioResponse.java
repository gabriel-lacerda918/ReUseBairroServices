package br.com.reusebairro.dto;

import br.com.reusebairro.domain.Usuario;
import java.time.LocalDateTime;

public record UsuarioResponse(Long id, String nome, String email, String bairro, LocalDateTime criadoEm) {
    public static UsuarioResponse de(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getBairro(), u.getCriadoEm());
    }
}
