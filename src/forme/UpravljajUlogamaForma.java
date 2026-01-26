package forme;

import domen.Uloga;
import forme.model.UlogaTableModel;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JTable;

public class UpravljajUlogamaForma extends javax.swing.JDialog {

    public UpravljajUlogamaForma(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setTitle("Upravljanje ulogama");
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableUloge = new javax.swing.JTable();
        jButtonDodaj = new javax.swing.JButton();
        jButtonIzmeni = new javax.swing.JButton();
        jButtonObrisi = new javax.swing.JButton();
        jButtonZatvori = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTableUloge.setModel(new UlogaTableModel());
        jTableUloge.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTableUloge);

        jButtonDodaj.setText("Dodaj");

        jButtonIzmeni.setText("Izmeni");

        jButtonObrisi.setText("Obriši");

        jButtonZatvori.setText("Zatvori");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonDodaj)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonIzmeni)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonObrisi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonZatvori)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonDodaj)
                    .addComponent(jButtonIzmeni)
                    .addComponent(jButtonObrisi)
                    .addComponent(jButtonZatvori))
                .addContainerGap())
        );

        pack();
    }

    public void setUloge(List<Uloga> uloge) {
        UlogaTableModel model = (UlogaTableModel) jTableUloge.getModel();
        model.setUloge(uloge);
    }

    public Uloga getIzabranaUloga() {
        int row = jTableUloge.getSelectedRow();
        if (row == -1) {
            return null;
        }
        UlogaTableModel model = (UlogaTableModel) jTableUloge.getModel();
        return model.vratiRed(row);
    }

    public JButton getjButtonDodaj() {
        return jButtonDodaj;
    }

    public JButton getjButtonIzmeni() {
        return jButtonIzmeni;
    }

    public JButton getjButtonObrisi() {
        return jButtonObrisi;
    }

    public JButton getjButtonZatvori() {
        return jButtonZatvori;
    }

    public JTable getjTableUloge() {
        return jTableUloge;
    }

    private javax.swing.JButton jButtonDodaj;
    private javax.swing.JButton jButtonIzmeni;
    private javax.swing.JButton jButtonObrisi;
    private javax.swing.JButton jButtonZatvori;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableUloge;
}
