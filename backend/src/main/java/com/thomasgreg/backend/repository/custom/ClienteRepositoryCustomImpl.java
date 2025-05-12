package com.thomasgreg.backend.repository.custom;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import com.microsoft.sqlserver.jdbc.SQLServerPreparedStatement;
import com.thomasgreg.backend.model.Cliente;
import com.thomasgreg.backend.model.Logradouro;

@Repository
public class ClienteRepositoryCustomImpl implements ClienteRepositoryCustom {

    @Autowired
    private DataSource dataSource;

    @Override
    public Long saveClienteProcedure(Cliente cliente) {
        try (Connection conn = dataSource.getConnection()) {
            SQLServerConnection sqlConn = conn.unwrap(SQLServerConnection.class);

            SQLServerDataTable logradourosTVP = new SQLServerDataTable();
            logradourosTVP.addColumnMetadata("logradouro", java.sql.Types.NVARCHAR);

            if (cliente.getLogradouros() != null) {
                for (Logradouro l : cliente.getLogradouros()) {
                    logradourosTVP.addRow(l.getLogradouro());
                }
            }

            String sql = "{call sp_salvar_cliente_completo(?, ?, ?, ?, ?)}";
            try (CallableStatement stmt = sqlConn.prepareCall(sql)) {
                stmt.setString(1, cliente.getNome());
                stmt.setString(2, cliente.getEmail());

                if (cliente.getLogotipo() != null) {
                    stmt.setBytes(3, cliente.getLogotipo().getLogotipo());
                    stmt.setString(4, cliente.getLogotipo().getTipoArquivo());
                } else {
                    stmt.setNull(3, Types.VARBINARY);
                    stmt.setNull(4, Types.NVARCHAR);
                }

                    ((SQLServerPreparedStatement) stmt).setStructured(5, "tipo_Logradouro", logradourosTVP);

                    boolean hasResultSet = stmt.execute();
                    if (hasResultSet) {
                        try (ResultSet rs = stmt.getResultSet()) {
                            if (rs.next()) {
                                return (Long)rs.getLong("cliente_id");
                            }
                        }
                    }else{
                        throw new RuntimeException("Erro ao salvar cliente via procedure");
                    }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar cliente via procedure", e);
        }
        return null;
    }
}