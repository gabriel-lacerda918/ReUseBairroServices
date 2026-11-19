package br.com.reusebairro.service;

import br.com.reusebairro.domain.Usuario;
import br.com.reusebairro.dto.UsuarioRequest;
import br.com.reusebairro.dto.UsuarioResponse;
import br.com.reusebairro.exception.RecursoNaoEncontradoException;
import br.com.reusebairro.exception.RegraNegocioException;
import br.com.reusebairro.repository.UsuarioRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UsuarioResponse criar(UsuarioRequest r) {
        if (repository.existsByEmailIgnoreCase(r.email())) {
            throw new RegraNegocioException("Já existe um usuário com o e-mail " + r.email());
        }
        Usuario u = new Usuario();
        u.setNome(r.nome().trim());
        u.setEmail(r.email().trim().toLowerCase());
        u.setBairro(r.bairro().trim());
        return UsuarioResponse.de(repository.save(u));
    }

    public List<UsuarioResponse> listar() {
        return repository.findAll().stream().map(UsuarioResponse::de).toList();
    }

    public UsuarioResponse buscar(Long id) {
        return UsuarioResponse.de(repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id)));
    }
}
