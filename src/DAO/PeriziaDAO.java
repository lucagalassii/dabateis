package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeriziaDAO {
    public boolean inserisciPerizia(int idPerizia, String data, double importo, String descrizione, int codPerito, int idSinistro) {
        String sql = "INSERT INTO PERIZIA (IDPerizia, Data, ImportoStimato, Descrizione, CodPerito, IdSinistro) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = database.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPerizia);
            pstmt.setString(2, data);
            pstmt.setDouble(3, importo);
            pstmt.setString(4, descrizione);
            pstmt.setInt(5, codPerito);
            pstmt.setInt(6, idSinistro);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<String[]> getPeriziePerSinistro(int idSinistro) {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT P.IDPerizia, P.Data, P.ImportoStimato, P.Descrizione, PE.Cognome AS CognomePerito " +
                     "FROM PERIZIA P JOIN PERITO PE ON P.CodPerito = PE.CodPerito WHERE P.IdSinistro = ?";
        try (Connection conn = database.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idSinistro);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new String[]{
                        String.valueOf(rs.getInt("IDPerizia")),
                        rs.getString("Data"),
                        String.valueOf(rs.getDouble("ImportoStimato")),
                        rs.getString("Descrizione"),
                        rs.getString("CognomePerito")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}