CREATE TABLE tb_user (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         username TEXT UNIQUE NOT NULL,
                         password TEXT NOT NULL,
                         roles smallint[],
                         created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tb_produto (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            nome TEXT NOT NULL,
                            preco DOUBLE PRECISION NOT NULL,
                            categoria TEXT NOT NULL,
                            created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tb_pedido (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tb_pedido_produto (
                                   pedido_id UUID REFERENCES tb_pedido(id),
                                   produto_id UUID REFERENCES tb_produto(id),
                                   PRIMARY KEY (produto_id, pedido_id)
);
