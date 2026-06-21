package DAO;

import java.sql.*;

public class SinistroDAO {
    public boolean inserisciSinistro(int idSinistro, String dataSinistro, double dannoStima, String numeroPolizza) {
        String sql = "INSERT INTO SINISTRO (IdSinistro, DataSinistro, Stato, DannoStima, NumeroPolizza) VALUES (?, ?, 'Aperto', ?, ?)";
        
        try (Connection conn = database.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idSinistro);
            pstmt.setString(2, dataSinistro); // Formato AAAA-MM-GG
            pstmt.setDouble(3, dannoStima);
            pstmt.setString(4, numeroPolizza);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}