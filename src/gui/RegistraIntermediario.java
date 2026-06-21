package gui;

import javax.swing.*;
import java.awt.*;

public class RegistraIntermediario extends JDialog {

    public RegistraIntermediario(JFrame parent) {
        super(parent, "Registra Nuovo Intermediario", true);
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlCampi = new JPanel(new GridLayout(6, 2, 10, 10));
        pnlCampi.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField txtCodice = new JTextField();
        JTextField txtNome = new JTextField();
        JTextField txtCognome = new JTextField();
        JTextField txtNascita = new JTextField("1985-01-01");
        JTextField txtLuogo = new JTextField();
        JTextField txtSpec = new JTextField("Agente");

        pnlCampi.add(new JLabel("Codice Intermediario (ID):")); pnlCampi.add(txtCodice);
        pnlCampi.add(new JLabel("Nome:")); pnlCampi.add(txtNome);
        pnlCampi.add(new JLabel("Cognome:")); pnlCampi.add(txtCognome);
        pnlCampi.add(new JLabel("Data Nascita (AAAA-MM-GG):")); pnlCampi.add(txtNascita);
        pnlCampi.add(new JLabel("Luogo di Nascita:")); pnlCampi.add(txtLuogo);
        pnlCampi.add(new JLabel("Specializzazione:")); pnlCampi.add(txtSpec);

        add(pnlCampi, BorderLayout.CENTER);

        JButton btnSalva = new JButton("Salva Intermediario");
        btnSalva.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalva.setBackground(new Color(173, 216, 230));
        
        btnSalva.addActionListener(e -> {
            try {
                int codice = Integer.parseInt(txtCodice.getText().trim());
                String nome = txtNome.getText().trim();
                String cognome = txtCognome.getText().trim();

                if (nome.isEmpty() || cognome.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nome e Cognome sono obbligatori!", "Attenzione", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                DAO.IntermediarioDAO dao = new DAO.IntermediarioDAO();
                boolean ok = dao.inserisciIntermediario(codice, nome, cognome, txtNascita.getText().trim(), txtLuogo.getText().trim(), txtSpec.getText().trim());

                if (ok) {
                    JOptionPane.showMessageDialog(this, "Intermediario inserito con successo!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Errore durante l'inserimento. Verifica se il Codice esiste già.", "Errore DB", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Il codice intermediario deve essere un numero intero.", "Errore Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(btnSalva, BorderLayout.SOUTH);
    }
}