package DAO;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Intermediario;

public class IntermediarioDAO {
        public Intermediario loginIntermediario(int id) {
            String sql = "SELECT CodiceIntermediario, Nome, Cognome, Nascita, LuogoNascita " +
                        "FROM INTERMEDIARIO WHERE CodiceIntermediario = ?";
            
            try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, id);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        Intermediario inter = new Intermediario();
                        inter.SetCodiceIntermediario(rs.getInt("CodiceIntermediario"));
                        inter.SetNome(rs.getString("Nome"));
                        inter.SetCognome(rs.getString("Cognome"));
                        inter.SetNascita(rs.getDate("Nascita").toLocalDate());
                        inter.SetLuogoNascita(rs.getString("LuogoNascita"));
                        return inter; 
                    }
                }
            } catch (SQLException e) {
                System.err.println("Errore di connessione locale: " + e.getMessage());
            }
        return null; 
    }

    public boolean inserisciIntermediario(int codice, String nome, String cognome, String nascita, String luogo, String specializzazione) {
        String sql = "INSERT INTO INTERMEDIARIO (CodiceIntermediario, Nome, Cognome, Nascita, LuogoNascita, Specializzazione) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = database.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, codice);
            pstmt.setString(2, nome);
            pstmt.setString(3, cognome);
            
            if (nascita == null || nascita.trim().isEmpty()) {
                pstmt.setNull(4, java.sql.Types.DATE);
            } else {
                pstmt.setString(4, nascita); // Formato AAAA-MM-GG
            }
            
            pstmt.setString(5, luogo);
            pstmt.setString(6, specializzazione);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
