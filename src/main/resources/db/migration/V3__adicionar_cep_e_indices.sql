ALTER TABLE item ADD COLUMN cep VARCHAR(8);
CREATE INDEX idx_item_status ON item (status);
CREATE INDEX idx_item_bairro ON item (bairro);
