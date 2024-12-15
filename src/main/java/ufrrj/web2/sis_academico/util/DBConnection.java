package ufrrj.aps.onstage.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBConnection {
    public static Connection getConection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/onstage?serverTimezone=UTC",
                "root",
                ""
            );
            
            if (conn == null) {
                throw new SQLException("Conexão não pode ser estabelecida");
            }
            
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            throw new SQLException("Erro ao conectar ao banco: " + e.getMessage());
        }
    }
}
