CREATE TABLE usuario (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    email     VARCHAR(150) NOT NULL UNIQUE,
    bairro    VARCHAR(80)  NOT NULL,
    criado_em TIMESTAMP    NOT NULL
);

CREATE TABLE item (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo        VARCHAR(120) NOT NULL,
    descricao     VARCHAR(500),
    categoria     VARCHAR(30)  NOT NULL,
    bairro        VARCHAR(80)  NOT NULL,
    endereco      VARCHAR(200) NOT NULL,
    status        VARCHAR(20)  NOT NULL,
    anunciante_id BIGINT       NOT NULL,
    coletor_id    BIGINT,
    criado_em     TIMESTAMP    NOT NULL,
    coletado_em   TIMESTAMP,
    CONSTRAINT fk_item_anunciante FOREIGN KEY (anunciante_id) REFERENCES usuario (id),
    CONSTRAINT fk_item_coletor    FOREIGN KEY (coletor_id)    REFERENCES usuario (id)
);
