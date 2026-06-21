package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PolizzaDAO {

    public List<String[]> getPreventiviNonAccettati() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT NumeroPreventivo, Prezzo, DataScadenza, CF FROM PREVENTIVO WHERE Stato = 'In attesa'";
        try (Connection conn = database.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                    String.valueOf(rs.getInt("NumeroPreventivo")),
                    String.valueOf(rs.getDouble("Prezzo")),
                    rs.getString("DataScadenza"),
                    rs.getString("CF")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<String[]> getPolizzeCliente(String cf) {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT P.NumeroPolizza, P.Prezzo, P.Frazionamento, P.DataFineEffetti FROM POLIZZA P " +
                     "JOIN PREVENTIVO PR ON P.NumeroPreventivo = PR.NumeroPreventivo WHERE PR.CF = ?";
        try (Connection conn = database.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cf.toUpperCase());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new String[]{
                        rs.getString("NumeroPolizza"), String.valueOf(rs.getDouble("Prezzo")),
                        rs.getString("Frazionamento"), rs.getString("DataFineEffetti")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean inserisciPreventivo(int numero, double prezzo, String scadenza, String cf, int idIntermediario) {
        String sql = "INSERT INTO PREVENTIVO (NumeroPreventivo, DataRichiesta, DataScadenza, Prezzo, Stato, CF, CodiceIntermediario) VALUES (?, CURDATE(), ?, ?, 'In attesa', ?, ?)";
        try (Connection conn = database.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, numero);
            pstmt.setString(2, scadenza);
            pstmt.setDouble(3, prezzo);
            pstmt.setString(4, cf.toUpperCase());
            pstmt.setInt(5, idIntermediario);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean accettaPreventivoReale(int numeroPreventivo, String numeroPolizza, String frazionamento, String dataInizio, String dataFine) {
        String sqlSel = "SELECT Prezzo FROM PREVENTIVO WHERE NumeroPreventivo = ? AND Stato = 'In attesa'";
        String sqlIns = "INSERT INTO POLIZZA (NumeroPolizza, Prezzo, Frazionamento, DataInizioEffetti, DataFineEffetti, NumeroPreventivo) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlUp = "UPDATE PREVENTIVO SET Stato = 'Accettato' WHERE NumeroPreventivo = ?";
        
        Connection conn = null;
        try {
            conn = database.DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            double prezzo = 0;
            try (PreparedStatement ps = conn.prepareStatement(sqlSel)) {
                ps.setInt(1, numeroPreventivo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) prezzo = rs.getDouble("Prezzo");
                    else return false;
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(sqlIns)) {
                ps.setString(1, numeroPolizza); ps.setDouble(2, prezzo); ps.setString(3, frazionamento);
                ps.setString(4, dataInizio); ps.setString(5, dataFine); ps.setInt(6, numeroPreventivo);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(sqlUp)) {
                ps.setInt(1, numeroPreventivo); ps.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) {} }
            e.printStackTrace();
            return false;
        }
    }

    public boolean rimuoviPolizza(String numeroPolizza) {
        String sql = "DELETE FROM POLIZZA WHERE NumeroPolizza = ?";
        try (Connection conn = database.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numeroPolizza);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}