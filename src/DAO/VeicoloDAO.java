package DAO;
import database.DBConnection;
import java.sql.*;

public class VeicoloDAO {
    public boolean inserisciVeicolo(String targa, String marca, String modello, String telaio, String tipo) {
        String sql = "INSERT INTO VEICOLO (Targa, Marca, Modello, NumeroTelaio, Tipologia) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, targa); pstmt.setString(2, marca);
            pstmt.setString(3, modello); pstmt.setString(4, telaio); pstmt.setString(5, tipo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}