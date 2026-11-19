package br.com.reusebairro.service;

import br.com.reusebairro.domain.Categoria;
import br.com.reusebairro.domain.Item;
import br.com.reusebairro.domain.StatusItem;
import br.com.reusebairro.domain.Usuario;
import br.com.reusebairro.dto.ItemRequest;
import br.com.reusebairro.dto.ItemResponse;
import br.com.reusebairro.exception.RecursoNaoEncontradoException;
import br.com.reusebairro.exception.RegraNegocioException;
import br.com.reusebairro.repository.ItemRepository;
import br.com.reusebairro.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;
    private final UsuarioRepository usuarioRepository;

    public ItemService(ItemRepository itemRepository, UsuarioRepository usuarioRepository) {
        this.itemRepository = itemRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ItemResponse criar(ItemRequest r) {
        Usuario anunciante = buscarUsuario(r.anuncianteId());
        Item item = new Item();
        item.setAnunciante(anunciante);
        item.setStatus(StatusItem.DISPONIVEL);
        preencher(item, r);
        return ItemResponse.de(itemRepository.save(item));
    }

    public List<ItemResponse> listar(Categoria categoria, String bairro, StatusItem status) {
        Specification<Item> spec = (root, query, cb) -> cb.conjunction();
        if (categoria != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("categoria"), categoria));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (bairro != null && !bairro.isBlank()) {
            String filtro = "%" + bairro.trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("bairro")), filtro));
        }
        return itemRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "id"))
                .stream().map(ItemResponse::de).toList();
    }

    public ItemResponse buscar(Long id) {
        return ItemResponse.de(buscarItem(id));
    }

    @Transactional
    public ItemResponse atualizar(Long id, ItemRequest r) {
        Item item = buscarItem(id);
        if (item.getStatus() == StatusItem.COLETADO) {
            throw new RegraNegocioException("Item já coletado não pode ser alterado");
        }
        preencher(item, r);
        return ItemResponse.de(itemRepository.save(item));
    }

    @Transactional
    public void remover(Long id) {
        itemRepository.delete(buscarItem(id));
    }

    /** Regra de negócio principal: registrar a coleta de um item disponível por outro usuário. */
    @Transactional
    public ItemResponse coletar(Long id, Long coletorId) {
        Item item = buscarItem(id);
        if (item.getStatus() == StatusItem.COLETADO) {
            throw new RegraNegocioException("Este item já foi coletado");
        }
        if (item.getAnunciante().getId().equals(coletorId)) {
            throw new RegraNegocioException("O anunciante não pode coletar o próprio item");
        }
        item.setColetor(buscarUsuario(coletorId));
        item.setStatus(StatusItem.COLETADO);
        item.setColetadoEm(LocalDateTime.now());
        return ItemResponse.de(itemRepository.save(item));
    }

    private void preencher(Item item, ItemRequest r) {
        item.setTitulo(r.titulo().trim());
        item.setDescricao(r.descricao());
        item.setCategoria(r.categoria());
        item.setBairro(r.bairro().trim());
        item.setEndereco(r.endereco().trim());
        item.setCep(r.cep());
    }

    private Item buscarItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item não encontrado: " + id));
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));
    }
}
