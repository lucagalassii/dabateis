package gui;

import javax.swing.*;
import java.awt.*;

public class RegistraPerito extends JDialog {

    public RegistraPerito(JFrame parent) {
        super(parent, "Registra Nuovo Perito Assicurativo", true);
        setSize(400, 280);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlCampi = new JPanel(new GridLayout(4, 2, 10, 10));
        pnlCampi.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField txtCodice = new JTextField();
        JTextField txtNome = new JTextField();
        JTextField txtCognome = new JTextField();
        JTextField txtSpec = new JTextField("Ingegneria Autoveicoli");

        pnlCampi.add(new JLabel("Codice Perito (ID):")); pnlCampi.add(txtCodice);
        pnlCampi.add(new JLabel("Nome:")); pnlCampi.add(txtNome);
        pnlCampi.add(new JLabel("Cognome:")); pnlCampi.add(txtCognome);
        pnlCampi.add(new JLabel("Specializzazione:")); pnlCampi.add(txtSpec);

        add(pnlCampi, BorderLayout.CENTER);

        JButton btnSalva = new JButton("Salva Perito");
        btnSalva.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalva.setBackground(new Color(255, 235, 156));
        
        btnSalva.addActionListener(e -> {
            try {
                int codice = Integer.parseInt(txtCodice.getText().trim());
                String nome = txtNome.getText().trim();
                String cognome = txtCognome.getText().trim();

                if (nome.isEmpty() || cognome.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nome e Cognome sono obbligatori!", "Attenzione", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                DAO.PeritoDAO dao = new DAO.PeritoDAO();
                boolean ok = dao.inserisciPerito(codice, nome, cognome, txtSpec.getText().trim());

                if (ok) {
                    JOptionPane.showMessageDialog(this, "Perito registrato con successo!");
                    dispose(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Errore. Il codice potrebbe essere già esistente.", "Errore DB", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Il codice perito deve essere un numero intero.", "Errore Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(btnSalva, BorderLayout.SOUTH);
    }
}