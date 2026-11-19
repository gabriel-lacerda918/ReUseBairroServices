package br.com.reusebairro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import br.com.reusebairro.domain.Item;
import br.com.reusebairro.domain.StatusItem;
import br.com.reusebairro.domain.Usuario;
import br.com.reusebairro.dto.ItemResponse;
import br.com.reusebairro.exception.RegraNegocioException;
import br.com.reusebairro.repository.ItemRepository;
import br.com.reusebairro.repository.UsuarioRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock ItemRepository itemRepository;
    @Mock UsuarioRepository usuarioRepository;
    @InjectMocks ItemService service;

    private Usuario anunciante;
    private Usuario coletor;
    private Item item;

    @BeforeEach
    void setUp() {
        anunciante = usuario(1L, "Marta");
        coletor = usuario(2L, "João");
        item = new Item();
        item.setId(10L);
        item.setTitulo("Sofá");
        item.setAnunciante(anunciante);
        item.setStatus(StatusItem.DISPONIVEL);
    }

    private Usuario usuario(Long id, String nome) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setNome(nome);
        return u;
    }

    @Test
    void anuncianteNaoPodeColetarProprioItem() {
        when(itemRepository.findById(10L)).thenReturn(Optional.of(item));
        assertThrows(RegraNegocioException.class, () -> service.coletar(10L, 1L));
    }

    @Test
    void itemJaColetadoNaoPodeSerColetadoDeNovo() {
        item.setStatus(StatusItem.COLETADO);
        when(itemRepository.findById(10L)).thenReturn(Optional.of(item));
        assertThrows(RegraNegocioException.class, () -> service.coletar(10L, 2L));
    }

    @Test
    void coletaValidaMarcaItemComoColetado() {
        when(itemRepository.findById(10L)).thenReturn(Optional.of(item));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(coletor));
        when(itemRepository.save(any(Item.class))).thenAnswer(inv -> inv.getArgument(0));

        ItemResponse resposta = service.coletar(10L, 2L);

        assertEquals(StatusItem.COLETADO, resposta.status());
        assertEquals(2L, resposta.coletorId());
        assertNotNull(resposta.coletadoEm());
    }
}
