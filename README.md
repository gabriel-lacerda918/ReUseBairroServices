# ReUseBairro API — CitizenTech (SOA e Web Services)
## FIAP - Engenharia de Software 
## Gabriel Lacerda - RM: 556714 - 3ESPW   



Camada de backend do **ReUseBairro**: plataforma para anunciar móveis e eletrônicos que seriam descartados na rua (gerando sujeira, poluição e alagamentos) e permitir que outra pessoa faça a coleta e o reaproveitamento.

## Ficha da CitizenTech
 
| | |
|---|---|
| Nome da CitizenTech | ReUseBairro |
| Cidadão beneficiado | Moradores do bairro que têm móveis e eletrônicos para descartar, e pessoas que podem aproveitar esses itens (reparar, reutilizar ou revender) |
| Problema, em uma frase | Móveis e eletrônicos descartados nas ruas geram sujeira, poluição e entopem bueiros, contribuindo para alagamentos |
| Como funciona | O cidadão se cadastra e publica o item com endereço de retirada. Outra pessoa encontra o anúncio (filtrando por categoria, bairro e status), combina a retirada e registra a coleta, e o item passa a constar como coletado |
| O que cada parte faz | **App mobile** (Expo/React Native): porta de entrada do cidadão, onde ele consulta, anuncia e coleta. **API REST** (este repositório): regras de negócio, validação e persistência. **Banco de dados** (H2 + Flyway): guarda usuários e itens, com o esquema versionado por migrações. **Swagger**: documentação interativa dos endpoints |
| Link do repositório | [https://github.com/‹usuario›/ReUseBairroServices](https://github.com/gabriel-lacerda918/ReUseBairroServices) |
| Indicador de impacto | Taxa de coleta = itens com status `COLETADO` ÷ total de itens anunciados. Cada item coletado é um item que deixou de ser descartado na rua. Pode ser consultado com `GET /api/itens?status=COLETADO` |
| ODS conectados | **ODS 11** (Cidades e Comunidades Sustentáveis), pela redução do lixo urbano e dos pontos de alagamento; **ODS 12** (Consumo e Produção Responsáveis), pelo reaproveitamento de itens em vez de descarte |

## Solução
API REST em **Java 17 + Spring Boot 3**, executada pelo terminal. É o backend do app mobile ReUseBairro (Expo/React Native), que pode consumir estes endpoints no lugar do mock.

| Requisito | Como foi atendido |
|---|---|
| **Estruturação do domínio (3,0)** | Camadas `controller → service → repository → domain`; entidades `Usuario` e `Item` (1 usuário anuncia N itens; 1 usuário coleta N itens); enums `Categoria` e `StatusItem`; DTOs com Bean Validation; tratamento global de erros; regra de negócio de coleta. |
| **Conexão com banco de dados (3,0)** | Spring Data JPA + banco **H2 em arquivo** (`./data/reusebairro`), dados persistem entre execuções. Console em `/h2-console`. |
| **Migrações (1,5)** | **Flyway**: `V1` cria tabelas, `V2` carga inicial, `V3` adiciona coluna `cep` e índices. O Hibernate não altera o esquema (`ddl-auto=none`). |
| **Documentação automática (1,5)** | **springdoc-openapi**: Swagger UI em `/swagger-ui.html` e OpenAPI em `/v3/api-docs`. Resumo em [`docs/ENDPOINTS.md`](docs/ENDPOINTS.md). |
| **Testes automatizados (1,0)** | JUnit 5 + Mockito (regras de negócio) e MockMvc + banco H2 em memória (integração dos endpoints). |

## Estrutura
```
src/main/java/br/com/reusebairro/
  controller/  endpoints REST          service/     regras de negócio
  repository/  acesso a dados (JPA)    domain/      entidades e enums
  dto/         contratos da API        exception/   erros padronizados
  config/      configuração OpenAPI
src/main/resources/db/migration/       migrações Flyway (V1, V2, V3)
src/test/java/                         testes unitários e de integração
requests.http                          requisições prontas (extensão REST Client)
scripts/evidencias.ps1                 gera as evidências automaticamente
docs/ENDPOINTS.md                      documentação dos endpoints
```

## Pré-requisitos
- **JDK 17 ou superior** (`java -version`)
- **Maven 3.9+** (`mvn -version`). No Windows: `winget install Apache.Maven` (reabra o terminal depois)
- **VS Code** com as extensões *Extension Pack for Java*, *Spring Boot Extension Pack* e *REST Client*

## Tecnologias
 
Java 17 · Spring Boot 3.3 · Spring Data JPA · H2 (banco em arquivo) · Flyway · springdoc-openapi (Swagger) · JUnit 5 · Mockito · MockMvc · Maven

## Como rodar (VS Code)
1. `Arquivo → Abrir Pasta…` e escolha a pasta `reusebairro-api`. Aguarde o Java importar o projeto.
2. Abra o terminal (`Ctrl+'`) e execute:
   ```bash
   mvn spring-boot:run
   ```
   Quando aparecer `Started ReUseBairroApplication`, a API está em http://localhost:8080.
3. Abra o Swagger UI: http://localhost:8080/swagger-ui.html
4. Para parar: `Ctrl+C` no terminal.

## Como rodar os testes
```bash
mvn test
```
Resultado esperado: `Tests run: 10, Failures: 0, Errors: 0` e `BUILD SUCCESS`.
(3 testes unitários em `ItemServiceTest` e 7 de integração em `ApiIntegrationTest`.)

## Como extrair as evidências
Crie a pasta `evidencias/` (o script faz isso) e reúna:

| # | Evidência | Como obter |
|---|---|---|
| 1 | Testes passando | `.\scripts\evidencias.ps1 testes` (gera `evidencias/01-testes-automatizados.txt`) + print do terminal com `BUILD SUCCESS` |
| 2 | API subindo + migrações | Print do terminal do `mvn spring-boot:run` mostrando `Successfully applied 3 migrations` e `Started ReUseBairroApplication` |
| 3 | Documentação Swagger | Print de http://localhost:8080/swagger-ui.html (com os endpoints expandidos) e `evidencias/02-openapi.json` |
| 4 | Chamadas REST funcionando | Com a API rodando, em outro terminal: `.\scripts\evidencias.ps1 api` (gera os `.json`). Ou use o `requests.http` e tire prints das respostas |
| 5 | Banco de dados e migrações | Abra http://localhost:8080/h2-console · JDBC URL `jdbc:h2:file:./data/reusebairro` · usuário `sa` · senha vazia. Rode `SELECT * FROM "flyway_schema_history";`, `SELECT * FROM item;` e tire prints |
| 6 | Erros tratados | Arquivos `08-erro-validacao-400.json` e `09-erro-nao-encontrado-404.json` |

Se o PowerShell bloquear scripts: `Set-ExecutionPolicy -Scope Process Bypass` e rode de novo.

## Prints / evidências
_Evidencias na pasta /img
