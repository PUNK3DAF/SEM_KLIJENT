/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import domen.Ansambl;
import forme.PrikazAnsambalaForma;
import forme.model.ModelTabeleAnsambl;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author vldmrk
 */
public class PrikazAnsambalaController {

    private final PrikazAnsambalaForma paf;

    public PrikazAnsambalaController(PrikazAnsambalaForma paf) {
        this.paf = paf;
        addActionListeners();
    }

    private void addActionListeners() {
        paf.addBtnObrisiActionListener(e -> handleObrisiAnsambl());
        paf.addBtnAzurirajActionListener(e -> handleAzurirajAnsambl());
        paf.addBtnPretragaActionListener(e -> handlePretraga());
        paf.addBtnPrikaziActionListener(e -> handlePrikaziAnsambl());
    }

    private void handleObrisiAnsambl() {
        Ansambl sel = getSelectedAnsambl();
        if (sel == null) return;

        ucitajAnsamblUPozadini(sel.getAnsamblID(), full -> {
            prikazi("Sistem je ucitao ansambl.");
            if (JOptionPane.showConfirmDialog(paf, "Da li ste sigurni da zelite da obrisete ansambl?",
                    "Potvrda brisanja", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                return;
            }
            brisiAnsamblUPozadini(full);
        });
    }

    private void handleAzurirajAnsambl() {
        Ansambl sel = getSelectedAnsambl();
        if (sel == null) return;

        ucitajAnsamblUPozadini(sel.getAnsamblID(), full -> {
            prikazi("Sistem je ucitao ansambl.");
            coordinator.Coordinator.getInstanca().dodajParam("Ansambl", full);
            coordinator.Coordinator.getInstanca().otvoriIzmeniAnsamblFormu();
        });
    }

    private void handlePretraga() {
        String ime = paf.getjTextFieldIme().getText().trim();
        String opis = paf.getjTextFieldOpis().getText().trim();
        String admin = paf.getjTextFieldAdmin().getText().trim();
        ModelTabeleAnsambl mta = (ModelTabeleAnsambl) paf.getjTableAnsambli().getModel();
        mta.pretrazi(ime, opis, admin);

        String poruka = mta.getLista() == null || mta.getLista().isEmpty()
                ? "Sistem ne moze da nadje ansamble po zadatoj vrednosti."
                : "Sistem je nasao ansamble po zadatoj vrednosti.";
        prikazi(poruka);
    }

    private void handlePrikaziAnsambl() {
        Ansambl sel = getSelectedAnsambl();
        if (sel == null) return;

        ucitajAnsamblUPozadini(sel.getAnsamblID(), full -> {
            String detalji = formatAnsambl(full);
            prikazi(detalji + "\n\nSistem je ucitao ansambl.", "Detalji ansambla");
        });
    }

    private Ansambl getSelectedAnsambl() {
        int red = paf.getjTableAnsambli().getSelectedRow();
        if (red == -1) {
            prikaziGresku("Sistem ne moze da ucita ansambl.");
            return null;
        }
        ModelTabeleAnsambl mta = (ModelTabeleAnsambl) paf.getjTableAnsambli().getModel();
        return mta.getLista().get(red);
    }

    private void ucitajAnsamblUPozadini(int id, java.util.function.Consumer<Ansambl> onSuccess) {
        new javax.swing.SwingWorker<Ansambl, Void>() {
            @Override
            protected Ansambl doInBackground() throws Exception {
                return komunikacija.Komunikacija.getInstanca().ucitajAnsambl(id);
            }

            @Override
            protected void done() {
                try {
                    onSuccess.accept(get());
                } catch (Exception ex) {
                    String razlog = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
                    prikaziGresku("Sistem ne moze da ucita ansambl", razlog);
                }
            }
        }.execute();
    }

    private void brisiAnsamblUPozadini(Ansambl ansambl) {
        new javax.swing.SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                komunikacija.Komunikacija.getInstanca().obrisiAnsambl(ansambl);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    prikazi("Sistem je obrisao ansambl.");
                    osveziFormu();
                    coordinator.Coordinator.getInstanca().osveziGlavnuFormu();
                } catch (Exception ex) {
                    String razlog = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
                    prikaziGresku("Sistem ne moze da obrise ansambl", razlog);
                }
            }
        }.execute();
    }

    private String formatAnsambl(Ansambl a) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(a.getAnsamblID()).append("\n");
        sb.append("Ime: ").append(a.getImeAnsambla() == null ? "" : a.getImeAnsambla()).append("\n");
        sb.append("Opis: ").append(a.getOpisAnsambla() == null ? "" : a.getOpisAnsambla()).append("\n");
        if (a.getAdmin() != null) {
            String ime = a.getAdmin().getAdminIme();
            String user = a.getAdmin().getAdminUsername();
            sb.append("Administrator: ");
            if (ime != null && !ime.isEmpty()) {
                sb.append(ime);
                if (user != null && !user.isEmpty()) {
                    sb.append(" (").append(user).append(")");
                }
            } else if (user != null && !user.isEmpty()) {
                sb.append(user);
            } else {
                sb.append("ID=").append(a.getAdmin().getAdminID());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void prikazi(String poruka) {
        prikazi(poruka, "Informacija");
    }

    private void prikazi(String poruka, String naslov) {
        JOptionPane.showMessageDialog(paf, poruka, naslov, JOptionPane.INFORMATION_MESSAGE);
    }

    private void prikaziGresku(String poruka) {
        prikaziGresku(poruka, null);
    }

    private void prikaziGresku(String poruka, String razlog) {
        String full = poruka;
        if (razlog != null && !razlog.isEmpty()) {
            full += "\nRazlog: " + razlog;
        }
        JOptionPane.showMessageDialog(paf, full, "Greska", JOptionPane.ERROR_MESSAGE);
    }

    public void otvoriFormu() {
        pripremiFormu();
        paf.setVisible(true);
    }

    public void pripremiFormu() {
        List<Ansambl> ansambli = komunikacija.Komunikacija.getInstanca().ucitajAnsamble();
        if (ansambli == null) {
            ansambli = new java.util.ArrayList<>();
        }
        ModelTabeleAnsambl mta = new ModelTabeleAnsambl(ansambli);
        paf.getjTableAnsambli().setModel(mta);
        paf.srediSirinuKolona(paf.getjTableAnsambli());
    }

    public void osveziFormu() {
        pripremiFormu();
    }
}