package br.com.reusebairro.controller;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1")
class ApiIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    private String itemJson(long anuncianteId, String titulo) {
        return """
                {"titulo":"%s","descricao":"Teste","categoria":"MOVEL","bairro":"Centro",
                 "endereco":"Rua A, 1","cep":"09000000","anuncianteId":%d}
                """.formatted(titulo, anuncianteId);
    }

    private long criarItem(long anuncianteId, String titulo) throws Exception {
        String corpo = mvc.perform(post("/api/itens").contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson(anuncianteId, titulo)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return mapper.readTree(corpo).get("id").asLong();
    }

    @Test
    void migracoesCarregamItensIniciais() throws Exception {
        mvc.perform(get("/api/itens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(4)));
    }

    @Test
    void criaItemComStatusDisponivel() throws Exception {
        mvc.perform(post("/api/itens").contentType(MediaType.APPLICATION_JSON).content(itemJson(1, "Mesa")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("DISPONIVEL"))
                .andExpect(jsonPath("$.anuncianteNome").value("Marta Souza"));
    }

    @Test
    void rejeitaItemInvalido() throws Exception {
        mvc.perform(post("/api/itens").contentType(MediaType.APPLICATION_JSON).content("{\"titulo\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes").isArray());
    }

    @Test
    void itemInexistenteRetorna404() throws Exception {
        mvc.perform(get("/api/itens/99999")).andExpect(status().isNotFound());
    }

    @Test
    void fluxoCompletoDeColeta() throws Exception {
        long id = criarItem(1, "Cadeira");
        String coleta = "{\"coletorId\":2}";

        mvc.perform(patch("/api/itens/" + id + "/coleta").contentType(MediaType.APPLICATION_JSON).content(coleta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COLETADO"))
                .andExpect(jsonPath("$.coletorId").value(2));

        mvc.perform(patch("/api/itens/" + id + "/coleta").contentType(MediaType.APPLICATION_JSON).content(coleta))
                .andExpect(status().isConflict());
    }

    @Test
    void removeItem() throws Exception {
        long id = criarItem(1, "Rack");
        mvc.perform(delete("/api/itens/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/itens/" + id)).andExpect(status().isNotFound());
    }

    @Test
    void emailDuplicadoRetornaConflito() throws Exception {
        String json = "{\"nome\":\"Teste\",\"email\":\"dup" + System.nanoTime() + "@teste.com\",\"bairro\":\"Centro\"}";
        mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict());
    }
}
