package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public List<String[]> getClientiSenzaCopertura() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT C.CF, C.Nome, C.Cognome, C.Telefono FROM CLIENTE C " +
                     "WHERE C.CF NOT IN (SELECT DISTINCT PR.CF FROM PREVENTIVO PR JOIN POLIZZA P ON PR.NumeroPreventivo = P.NumeroPreventivo)";
                     
        try (Connection conn = database.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                    rs.getString("CF"), rs.getString("Nome"), rs.getString("Cognome"), rs.getString("Telefono")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean inserisciCliente(String cf, String nome, String cognome, String nascita, String luogoNascita, String telefono) {
        String sql = "INSERT INTO CLIENTE (CF, Nome, Cognome, Nascita, LuogoNascita, Telefono) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = database.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cf.toUpperCase());
            pstmt.setString(2, nome);
            pstmt.setString(3, cognome);
            if (nascita == null || nascita.trim().isEmpty()) {
                pstmt.setNull(4, java.sql.Types.DATE);
            } else {
                pstmt.setString(4, nascita);
            }
            pstmt.setString(5, luogoNascita);
            pstmt.setString(6, telefono);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}