package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private JPanel pnlContenutoTabella;

    public DashboardFrame() {
        setTitle("Gestionale Assicurativo - Coerenza Completa Database SQL");
        setSize(1400, 950);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlSuperiore = new JPanel(new BorderLayout());
        pnlSuperiore.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblTitolo = new JLabel("Pannello Operativo Agenzia Assicurativa");
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 22));
        pnlSuperiore.add(lblTitolo, BorderLayout.WEST);

        JPanel pnlBottoniRapidi = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        JButton btnNuovoIntermediario = new JButton("+ Intermediario");
        btnNuovoIntermediario.setFont(new Font("Arial", Font.BOLD, 12));
        btnNuovoIntermediario.setBackground(new Color(230, 240, 250));
        btnNuovoIntermediario.addActionListener(e -> {
            new RegistraIntermediario(this).setVisible(true);
        });

        JButton btnNuovoPerito = new JButton("+ Perito");
        btnNuovoPerito.setFont(new Font("Arial", Font.BOLD, 12));
        btnNuovoPerito.setBackground(new Color(255, 242, 204));
        btnNuovoPerito.addActionListener(e -> {
            new RegistraPerito(this).setVisible(true);
        });

        pnlBottoniRapidi.add(btnNuovoIntermediario);
        pnlBottoniRapidi.add(btnNuovoPerito);
        
        pnlSuperiore.add(pnlBottoniRapidi, BorderLayout.EAST);
        add(pnlSuperiore, BorderLayout.NORTH);

        JPanel pnlMainGrid = new JPanel(new GridLayout(2, 3, 15, 15));
        pnlMainGrid.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        pnlMainGrid.add(creaPannelloAggiungiCliente());
        pnlMainGrid.add(creaPannelloNuovoPreventivo());
        pnlMainGrid.add(creaPannelloAccettaPreventivo());
        pnlMainGrid.add(creaPannelloRimuoviPolizza());
        pnlMainGrid.add(creaPannelloApriSinistro());
        pnlMainGrid.add(creaPannelloAggiungiPerizia()); 

        add(pnlMainGrid, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new BorderLayout(5, 5));
        pnlBottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlBottom.setPreferredSize(new Dimension(1400, 350)); 

        JPanel pnlFiltri = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        pnlFiltri.add(new JLabel("Visualizza report/operazione:"));
        
        String[] opzioni = {
            "Seleziona operazione...", "Clienti Senza Copertura", 
            "Preventivi Pendenti", "Polizze per Cliente", "Perizie di un Sinistro"
        };
        JComboBox<String> cbVis = new JComboBox<>(opzioni);
        pnlFiltri.add(cbVis);
        JButton btnEsegui = new JButton("Esegui");
        pnlFiltri.add(btnEsegui);
        pnlBottom.add(pnlFiltri, BorderLayout.NORTH);

        pnlContenutoTabella = new JPanel(new BorderLayout());
        pnlContenutoTabella.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JLabel lblPlaceholder = new JLabel("Scegli un'operazione dal menu sopra e clicca 'Esegui'.", JLabel.CENTER);
        lblPlaceholder.setForeground(Color.GRAY);
        pnlContenutoTabella.add(lblPlaceholder, BorderLayout.CENTER);
        pnlBottom.add(pnlContenutoTabella, BorderLayout.CENTER);

        btnEsegui.addActionListener(e -> {
            pnlContenutoTabella.removeAll();
            String scelta = cbVis.getSelectedItem().toString();
            switch (scelta) {
                case "Clienti Senza Copertura": pnlContenutoTabella.add(creaSezioneClientiSenzaCopertura(), BorderLayout.CENTER); break;
                case "Preventivi Pendenti": pnlContenutoTabella.add(creaSezionePreventivi(), BorderLayout.CENTER); break;
                case "Polizze per Cliente": pnlContenutoTabella.add(creaSezionePolizzeCliente(), BorderLayout.CENTER); break;
                case "Perizie di un Sinistro": pnlContenutoTabella.add(creaSezionePerizieSinistro(), BorderLayout.CENTER); break;
                default:
                    pnlContenutoTabella.add(new JLabel("Seleziona un'opzione valida.", JLabel.CENTER), BorderLayout.CENTER);
                    break;
            }
            pnlContenutoTabella.revalidate(); pnlContenutoTabella.repaint();
        });
        add(pnlBottom, BorderLayout.SOUTH);
    }

    private JPanel creaPannelloAggiungiCliente() {
        JPanel p = new JPanel(new GridLayout(7, 2, 5, 5));
        p.setBorder(BorderFactory.createTitledBorder("1. Registra Cliente (Entità CLIENTE)"));
        JTextField txtCF = new JTextField(); JTextField txtNome = new JTextField(); JTextField txtCognome = new JTextField();
        JTextField txtNascita = new JTextField("1990-01-01"); JTextField txtLuogo = new JTextField(); JTextField txtTel = new JTextField();
        JButton btnInvia = new JButton("Salva Cliente");

        p.add(new JLabel("Codice Fiscale (CF):")); p.add(txtCF); p.add(new JLabel("Nome:")); p.add(txtNome);
        p.add(new JLabel("Cognome:")); p.add(txtCognome); p.add(new JLabel("Data Nascita (AAAA-MM-GG):")); p.add(txtNascita);
        p.add(new JLabel("Luogo Nascita:")); p.add(txtLuogo); p.add(new JLabel("Telefono:")); p.add(txtTel);
        p.add(new JLabel("")); p.add(btnInvia);

        btnInvia.addActionListener(e -> {
            if(txtCF.getText().trim().length() != 16) {
                JOptionPane.showMessageDialog(this, "Il CF deve essere di 16 caratteri."); return;
            }
            DAO.ClienteDAO dao = new DAO.ClienteDAO();
            if(dao.inserisciCliente(txtCF.getText(), txtNome.getText(), txtCognome.getText(), txtNascita.getText(), txtLuogo.getText(), txtTel.getText())) {
                JOptionPane.showMessageDialog(this, "Cliente salvato.");
                txtCF.setText(""); txtNome.setText(""); txtCognome.setText(""); txtLuogo.setText(""); txtTel.setText("");
            } else { JOptionPane.showMessageDialog(this, "Errore salvataggio.", "Errore", JOptionPane.ERROR_MESSAGE); }
        });
        return p;
    }

    private JPanel creaPannelloNuovoPreventivo() {
        JPanel p = new JPanel(new GridLayout(6, 2, 5, 5));
        p.setBorder(BorderFactory.createTitledBorder("2. Richiedi Preventivo (Entità PREVENTIVO)"));
        JTextField txtNum = new JTextField(); JTextField txtPrezzo = new JTextField(); JTextField txtScadenza = new JTextField("2026-12-31");
        JTextField txtCF = new JTextField(); JTextField txtInterm = new JTextField("1");
        JButton btnInvia = new JButton("Salva Proposta");

        p.add(new JLabel("Numero Preventivo:")); p.add(txtNum); p.add(new JLabel("Prezzo Tariffa (€):")); p.add(txtPrezzo);
        p.add(new JLabel("Scadenza (AAAA-MM-GG):")); p.add(txtScadenza); p.add(new JLabel("CF Cliente:")); p.add(txtCF);
        p.add(new JLabel("Codice Intermediario:")); p.add(txtInterm); p.add(new JLabel("")); p.add(btnInvia);

        btnInvia.addActionListener(e -> {
            try {
                int num = Integer.parseInt(txtNum.getText().trim());
                double prezzo = Double.parseDouble(txtPrezzo.getText().trim());
                int interm = Integer.parseInt(txtInterm.getText().trim());
                DAO.PolizzaDAO dao = new DAO.PolizzaDAO();
                if(dao.inserisciPreventivo(num, prezzo, txtScadenza.getText(), txtCF.getText(), interm)) {
                    JOptionPane.showMessageDialog(this, "Preventivo registrato 'In attesa'.");
                    txtNum.setText(""); txtPrezzo.setText(""); txtCF.setText("");
                } else { JOptionPane.showMessageDialog(this, "Errore vincoli FK.", "Errore", JOptionPane.ERROR_MESSAGE); }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Valori numerici errati."); }
        });
        return p;
    }

    private JPanel creaPannelloAccettaPreventivo() {
        JPanel p = new JPanel(new GridLayout(6, 2, 5, 5));
        p.setBorder(BorderFactory.createTitledBorder("3. Accetta Proposta (Relazione GENERA)"));
        JTextField txtNumPrev = new JTextField(); JTextField txtNumPol = new JTextField(); JTextField txtFraz = new JTextField("Annuale");
        JTextField txtInizio = new JTextField("2026-06-21"); JTextField txtFine = new JTextField("2027-06-21");
        JButton btnInvia = new JButton("Emetti Polizza"); btnInvia.setBackground(new Color(210, 235, 210));

        p.add(new JLabel("Numero Preventivo:")); p.add(txtNumPrev); p.add(new JLabel("Nuovo Numero Polizza:")); p.add(txtNumPol);
        p.add(new JLabel("Frazionamento:")); p.add(txtFraz); p.add(new JLabel("Inizio Effetti:")); p.add(txtInizio);
        p.add(new JLabel("Fine Effetti:")); p.add(txtFine); p.add(new JLabel("")); p.add(btnInvia);

        btnInvia.addActionListener(e -> {
            try {
                int numPrev = Integer.parseInt(txtNumPrev.getText().trim());
                DAO.PolizzaDAO dao = new DAO.PolizzaDAO();
                if(dao.accettaPreventivoReale(numPrev, txtNumPol.getText().trim(), txtFraz.getText(), txtInizio.getText(), txtFine.getText())) {
                    JOptionPane.showMessageDialog(this, "Polizza emessa correttamente.");
                    txtNumPrev.setText(""); txtNumPol.setText("");
                } else { JOptionPane.showMessageDialog(this, "Preventivo non valido o già accettato.", "Errore", JOptionPane.ERROR_MESSAGE); }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Controlla i campi."); }
        });
        return p;
    }

    private JPanel creaPannelloRimuoviPolizza() {
        JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
        p.setBorder(BorderFactory.createTitledBorder("4. Annulla Contratto (Rimuovi POLIZZA)"));
        JTextField txtNumPol = new JTextField(); JButton btnElimina = new JButton("Revoca Definitivamente");
        btnElimina.setBackground(new Color(245, 200, 200));

        p.add(new JLabel("Numero Polizza:")); p.add(txtNumPol); p.add(new JLabel("")); p.add(new JLabel(""));
        p.add(new JLabel("")); p.add(btnElimina);

        btnElimina.addActionListener(e -> {
            if(new DAO.PolizzaDAO().rimuoviPolizza(txtNumPol.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Contratto rimosso."); txtNumPol.setText("");
            } else { JOptionPane.showMessageDialog(this, "Polizza non trovata o vincolata da Sinistri."); }
        });
        return p;
    }

    private JPanel creaPannelloApriSinistro() {
        JPanel p = new JPanel(new GridLayout(5, 2, 5, 5));
        p.setBorder(BorderFactory.createTitledBorder("5. Denuncia Sinistro (Relazione COPRE)"));
        JTextField txtId = new JTextField(); JTextField txtData = new JTextField("2026-06-21");
        JTextField txtDanno = new JTextField(); JTextField txtPol = new JTextField();
        JButton btnInvia = new JButton("Registra Sinistro"); btnInvia.setBackground(new Color(255, 204, 153));

        p.add(new JLabel("ID Sinistro (Intero):")); p.add(txtId); p.add(new JLabel("Data Accadimento:")); p.add(txtData);
        p.add(new JLabel("Stima Danno (€):")); p.add(txtDanno); p.add(new JLabel("Numero Polizza:")); p.add(txtPol);
        p.add(new JLabel("")); p.add(btnInvia);

        btnInvia.addActionListener(e -> {
            try {
                if(new DAO.SinistroDAO().inserisciSinistro(Integer.parseInt(txtId.getText().trim()), txtData.getText(), Double.parseDouble(txtDanno.getText().trim()), txtPol.getText().trim())) {
                    JOptionPane.showMessageDialog(this, "Sinistro aperto."); txtId.setText(""); txtDanno.setText(""); txtPol.setText("");
                } else { JOptionPane.showMessageDialog(this, "Errore. Polizza non esistente."); }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Dati numerici errati."); }
        });
        return p;
    }

    private JPanel creaPannelloAggiungiPerizia() {
        JPanel p = new JPanel(new GridLayout(6, 2, 5, 5));
        p.setBorder(BorderFactory.createTitledBorder("6. Registra Perizia (Entità PERIZIA)"));
        JTextField txtIdPerizia = new JTextField(); JTextField txtImporto = new JTextField(); JTextField txtDesc = new JTextField();
        JTextField txtCodPerito = new JTextField(); JTextField txtIdSinistro = new JTextField();
        JButton btnInvia = new JButton("Salva Perizia"); btnInvia.setBackground(new Color(255, 235, 156));

        p.add(new JLabel("ID Perizia:")); p.add(txtIdPerizia); p.add(new JLabel("Stima Importo (€):")); p.add(txtImporto);
        p.add(new JLabel("Descrizione:")); p.add(txtDesc); p.add(new JLabel("Codice Perito:")); p.add(txtCodPerito);
        p.add(new JLabel("ID Sinistro:")); p.add(txtIdSinistro); p.add(new JLabel("")); p.add(btnInvia);

        btnInvia.addActionListener(e -> {
            try {
                if(new DAO.PeriziaDAO().inserisciPerizia(Integer.parseInt(txtIdPerizia.getText().trim()), "2026-06-21", Double.parseDouble(txtImporto.getText().trim()), txtDesc.getText(), Integer.parseInt(txtCodPerito.getText().trim()), Integer.parseInt(txtIdSinistro.getText().trim()))) {
                    JOptionPane.showMessageDialog(this, "Perizia salvata.");
                    txtIdPerizia.setText(""); txtImporto.setText(""); txtDesc.setText(""); txtCodPerito.setText(""); txtIdSinistro.setText("");
                } else { JOptionPane.showMessageDialog(this, "Errore Vincoli FK (Perito/Sinistro)."); }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Errore formattazione numerica."); }
        });
        return p;
    }

    private JPanel creaSezioneClientiSenzaCopertura() {
        JPanel p = new JPanel(new BorderLayout()); String[] colonne = {"CF", "Nome", "Cognome", "Telefono"};
        DefaultTableModel model = new DefaultTableModel(colonne, 0); JTable tabella = new JTable(model);
        for (String[] riga : new DAO.ClienteDAO().getClientiSenzaCopertura()) model.addRow(riga);
        p.add(new JScrollPane(tabella), BorderLayout.CENTER); return p;
    }

    private JPanel creaSezionePreventivi() {
        JPanel p = new JPanel(new BorderLayout()); String[] colonne = {"Num Preventivo", "Prezzo (€)", "Scadenza", "CF Cliente"};
        DefaultTableModel model = new DefaultTableModel(colonne, 0); JTable tabella = new JTable(model);
        for (String[] riga : new DAO.PolizzaDAO().getPreventiviNonAccettati()) model.addRow(riga);
        p.add(new JScrollPane(tabella), BorderLayout.CENTER); return p;
    }

    private JPanel creaSezionePolizzeCliente() {
        JPanel pnl = new JPanel(new BorderLayout()); JPanel pnlCerca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtCF = new JTextField(16); JButton btn = new JButton("Cerca");
        pnlCerca.add(new JLabel("CF Cliente:")); pnlCerca.add(txtCF); pnlCerca.add(btn); pnl.add(pnlCerca, BorderLayout.NORTH);
        String[] col = {"Numero Polizza", "Premio (€)", "Frazionamento", "Fine Copertura"};
        DefaultTableModel model = new DefaultTableModel(col, 0); JTable tab = new JTable(model); pnl.add(new JScrollPane(tab), BorderLayout.CENTER);
        btn.addActionListener(e -> { model.setRowCount(0); for (String[] r : new DAO.PolizzaDAO().getPolizzeCliente(txtCF.getText().trim())) model.addRow(r); });
        return pnl;
    }

    private JPanel creaSezionePerizieSinistro() {
        JPanel pnl = new JPanel(new BorderLayout()); JPanel pnlCerca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtSin = new JTextField(10); JButton btn = new JButton("Cerca");
        pnlCerca.add(new JLabel("ID Sinistro:")); pnlCerca.add(txtSin); pnlCerca.add(btn); pnl.add(pnlCerca, BorderLayout.NORTH);
        String[] col = {"ID Perizia", "Data", "Importo (€)", "Descrizione", "Perito"};
        DefaultTableModel model = new DefaultTableModel(col, 0); JTable tab = new JTable(model); pnl.add(new JScrollPane(tab), BorderLayout.CENTER);
        btn.addActionListener(e -> {
            try { model.setRowCount(0); for (String[] r : new DAO.PeriziaDAO().getPeriziePerSinistro(Integer.parseInt(txtSin.getText().trim()))) model.addRow(r); } catch(Exception ex){}
        });
        return pnl;
    }
}