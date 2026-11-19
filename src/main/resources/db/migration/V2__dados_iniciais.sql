INSERT INTO usuario (nome, email, bairro, criado_em) VALUES
  ('Marta Souza', 'marta@reusebairro.com', 'Vila Assunção', CURRENT_TIMESTAMP),
  ('João Pereira', 'joao@reusebairro.com', 'Centro', CURRENT_TIMESTAMP);

INSERT INTO item (titulo, descricao, categoria, bairro, endereco, status, anunciante_id, criado_em) VALUES
  ('Sofá de 3 lugares', 'Bom estado, precisa de limpeza. Retirar com 2 pessoas.', 'MOVEL', 'Vila Assunção', 'Rua das Flores, 120', 'DISPONIVEL', 1, CURRENT_TIMESTAMP),
  ('Monitor LCD 19"', 'Funcionando, sem cabo HDMI.', 'ELETRONICO', 'Centro', 'Av. Industrial, 45', 'DISPONIVEL', 2, CURRENT_TIMESTAMP),
  ('Geladeira antiga', 'Não gela mais, serve para peças.', 'ELETRODOMESTICO', 'Jardim', 'Rua Verde, 8', 'DISPONIVEL', 1, CURRENT_TIMESTAMP),
  ('Guarda-roupa 4 portas', 'Madeira, pequeno desgaste.', 'MOVEL', 'Vila Assunção', 'Rua Azul, 300', 'DISPONIVEL', 2, CURRENT_TIMESTAMP);
