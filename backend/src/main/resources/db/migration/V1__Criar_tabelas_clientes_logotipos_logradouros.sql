DROP TABLE IF EXISTS clientes, logotipos, logradouros;
DROP PROCEDURE IF EXISTS sp_salvar_cliente_completo;
DROP TYPE IF EXISTS tipo_logradouro;

CREATE TABLE clientes (
    id BIGINT IDENTITY PRIMARY KEY,
    nome NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE logotipos (
    id BIGINT IDENTITY PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    logotipo VARBINARY(MAX),
    tipo_arquivo NVARCHAR(20),
    CONSTRAINT fk_logotipos_clientes FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE logradouros (
    id BIGINT IDENTITY PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    logradouro NVARCHAR(150),
    CONSTRAINT fk_logradouros_clientes FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);