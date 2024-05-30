INSERT INTO usuario (nome, email, senha_hash) VALUES
('Maria Silva', 'maria.silva@example.com', '1819aab14c4bed2d4f4476e4e5f1e598a59cbf72ca04840850c42738352a5d74ee2a90b8e1747957adac70a44a148c0193d3929af79ecda3074fc47f31e204c5'),
('João Santos', 'joao.santos@example.com', '1819aab14c4bed2d4f4476e4e5f1e598a59cbf72ca04840850c42738352a5d74ee2a90b8e1747957adac70a44a148c0193d3929af79ecda3074fc47f31e204c5'),
('Ana Pereira', 'ana.pereira@example.com', '1819aab14c4bed2d4f4476e4e5f1e598a59cbf72ca04840850c42738352a5d74ee2a90b8e1747957adac70a44a148c0193d3929af79ecda3074fc47f31e204c5');

INSERT INTO ingrediente (nome, categoria) VALUES
('Farinha de Trigo', 1),
('Açúcar', 1),
('Leite', 2),
('Ovo', 2);

INSERT INTO despensa (email, nome_ingrediente, validade, quantidade) VALUES
('maria.silva@example.com', 'Farinha de Trigo', '2024-12-01', 2),
('maria.silva@example.com', 'Açúcar', '2024-11-15', 1),
('joao.santos@example.com', 'Leite', '2024-06-10', 3),
('ana.pereira@example.com', 'Ovo', '2024-07-20', 12);

INSERT INTO receita (titulo, descricao, modo_preparo) VALUES
('Bolo de Chocolate', 'Um delicioso bolo de chocolate', 'Assar por 45 minutos'),
('Panqueca', 'Panqueca simples e rápida', 'Cozinhar por 10 minutos');

INSERT INTO receita_ingrediente (id_receita, nome_ingrediente, quantidade) VALUES
(1, 'Farinha de Trigo', 2),
(1, 'Açúcar', 1),
(2, 'Leite', 1),
(2, 'Ovo', 2);
