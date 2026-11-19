# Documentação dos Endpoints — ReUseBairro API

A documentação **interativa e gerada automaticamente** (OpenAPI 3 via springdoc) fica disponível com a API rodando:

| Recurso | URL |
|---|---|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI (JSON) | http://localhost:8080/v3/api-docs |

Este arquivo é o resumo equivalente em texto.

## Usuários — `/api/usuarios`

| Método | Rota | Descrição | Sucesso | Erros |
|---|---|---|---|---|
| POST | `/api/usuarios` | Cadastra usuário | 201 | 400 (validação), 409 (e-mail duplicado) |
| GET | `/api/usuarios` | Lista usuários | 200 | — |
| GET | `/api/usuarios/{id}` | Busca por id | 200 | 404 |

Corpo do POST: `{ "nome": "Carla", "email": "carla@email.com", "bairro": "Jardim" }`

## Itens — `/api/itens`

| Método | Rota | Descrição | Sucesso | Erros |
|---|---|---|---|---|
| POST | `/api/itens` | Publica item para coleta | 201 | 400, 404 (anunciante) |
| GET | `/api/itens` | Lista itens. Filtros: `categoria`, `bairro`, `status` | 200 | 400 (filtro inválido) |
| GET | `/api/itens/{id}` | Busca por id | 200 | 404 |
| PUT | `/api/itens/{id}` | Atualiza item disponível | 200 | 400, 404, 409 (já coletado) |
| DELETE | `/api/itens/{id}` | Remove item | 204 | 404 |
| PATCH | `/api/itens/{id}/coleta` | Registra a coleta | 200 | 400, 404, 409 |

Corpo do POST/PUT:
```json
{
  "titulo": "Sofá", "descricao": "Bom estado", "categoria": "MOVEL",
  "bairro": "Centro", "endereco": "Rua A, 1", "cep": "09000000", "anuncianteId": 1
}
```
Corpo do PATCH de coleta: `{ "coletorId": 2 }`

**Valores válidos:** `categoria` = `ELETRONICO`, `MOVEL`, `ELETRODOMESTICO`, `OUTRO`; `status` = `DISPONIVEL`, `COLETADO`.

**Regras de negócio da coleta (409):** item já coletado não pode ser coletado de novo nem alterado; o anunciante não pode coletar o próprio item.

## Formato de erro (padrão)
```json
{
  "status": 400, "erro": "Bad Request", "mensagem": "Dados inválidos",
  "detalhes": ["titulo: não deve estar em branco"], "timestamp": "2026-11-19T10:00:00"
}
```
