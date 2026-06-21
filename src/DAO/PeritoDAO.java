package DAO;

import java.sql.*;

public class PeritoDAO {

    public boolean inserisciPerito(int codPerito, String nome, String cognome, String specializzazione) {
        String sql = "INSERT INTO PERITO (CodPerito, Nome, Cognome, Specializzazione) VALUES (?, ?, ?, ?)";
        try (Connection conn = database.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, codPerito);
            pstmt.setString(2, nome);
            pstmt.setString(3, cognome);
            pstmt.setString(4, specializzazione);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}