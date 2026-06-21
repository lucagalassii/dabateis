package gui;

import DAO.IntermediarioDAO;
import Model.Intermediario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField txtIdDipendente;
    private JButton btnAccedi;
    private IntermediarioDAO intermediarioDAO;

    public LoginFrame() {
        intermediarioDAO = new IntermediarioDAO();
        
        setTitle("Gestionale Assicurazioni - Login Locale");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra lo schermo
        setLayout(new BorderLayout(10, 10));

        JPanel pnlCentro = new JPanel(new GridLayout(2, 1, 5, 5));
        pnlCentro.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        
        JLabel lblInfo = new JLabel("Inserisci il tuo ID Dipendente Univoco:", JLabel.CENTER);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 13));
        txtIdDipendente = new JTextField();
        txtIdDipendente.setFont(new Font("Arial", Font.PLAIN, 16));
        txtIdDipendente.setHorizontalAlignment(JTextField.CENTER);

        pnlCentro.add(lblInfo);
        pnlCentro.add(txtIdDipendente);

        JPanel pnlSud = new JPanel();
        btnAccedi = new JButton("Verifica e Accedi");
        btnAccedi.setFont(new Font("Arial", Font.BOLD, 14));
        pnlSud.add(btnAccedi);

        add(pnlCentro, BorderLayout.CENTER);
        add(pnlSud, BorderLayout.SOUTH);

        btnAccedi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                effettuarelLogin();
            }
        });
    }

    private void effettuarelLogin() {
        String inputStr = txtIdDipendente.getText().trim();
        
        if (inputStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Il campo ID non può essere vuoto!", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(inputStr);
            
            Intermediario operatore = intermediarioDAO.loginIntermediario(id);

            if (operatore != null) {
                JOptionPane.showMessageDialog(this, "Benvenuto, " + operatore.GetNome() + " " + operatore.GetCognome(), "Autenticazione Riuscita", JOptionPane.INFORMATION_MESSAGE);
                
                DashboardFrame dashboard = new DashboardFrame();
                dashboard.setVisible(true);
                
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "ID Dipendente non trovato in archivio locale.", "Accesso Negato", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "L'ID deve essere un valore numerico intero!", "Errore Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
