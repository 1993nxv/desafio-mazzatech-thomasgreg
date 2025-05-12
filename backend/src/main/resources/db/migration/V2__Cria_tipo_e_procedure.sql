GO
CREATE TYPE tipo_logradouro AS TABLE (
    logradouro NVARCHAR(150)
);
GO

GO
CREATE OR ALTER PROCEDURE sp_salvar_cliente_completo
    @nome NVARCHAR(100),
    @email NVARCHAR(100),
    @logotipo VARBINARY(MAX),
    @tipoArquivo NVARCHAR(20),
    @logradouros tipo_logradouro READONLY
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @clienteId BIGINT;

    INSERT INTO clientes (nome, email)
    VALUES (@nome, @email);

    SET @clienteId = SCOPE_IDENTITY();

    IF @logotipo IS NOT NULL
    BEGIN
        INSERT INTO logotipos (logotipo, tipo_arquivo, cliente_id)
        VALUES (@logotipo, @tipoArquivo, @clienteId);
    END

    INSERT INTO logradouros (logradouro, cliente_id)
    SELECT logradouro, @clienteId
    FROM @logradouros;

    SELECT @clienteId AS cliente_id;
END;
GO