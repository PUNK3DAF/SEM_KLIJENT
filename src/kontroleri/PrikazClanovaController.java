package kontroleri;

import domen.ClanDrustva;
import domen.Ucesce;
import forme.PrikazClanovaForma;
import forme.UIHelper;
import forme.model.ModelTabeleClan;
import java.util.List;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.TableColumnModel;

public class PrikazClanovaController {

    private final PrikazClanovaForma pcf;

    public PrikazClanovaController(PrikazClanovaForma pcf) {
        this.pcf = pcf;
        addActionListeners();
    }

    private void addActionListeners() {
        pcf.addBtnObrisiActionListener(e -> handleObrisiClan());
        pcf.addBtnAzurirajActionListener(e -> handleAzurirajClan());
        pcf.addBtnPretragaActionListener(e -> handlePretraga());
        pcf.addBtnPrikaziActionListener(e -> handlePrikazi());
    }

    private void handleObrisiClan() {
        int red = pcf.getjTableClanovi().getSelectedRow();
        if (red == -1) {
            UIHelper.showError(pcf, "Sistem ne može da učita člana društva.");
            return;
        }
        ModelTabeleClan mtc = (ModelTabeleClan) pcf.getjTableClanovi().getModel();
        ClanDrustva c = mtc.getLista().get(red);

        new SwingWorker<ClanDrustva, Void>() {
            @Override
            protected ClanDrustva doInBackground() throws Exception {
                return komunikacija.Komunikacija.getInstanca().ucitajClanaDrustva(c.getClanID());
            }

            @Override
            protected void done() {
                try {
                    ClanDrustva full = get();
                    UIHelper.showInfo(pcf, "Sistem je učitao člana društva.");
                    if (UIHelper.confirm(pcf, "Da li ste sigurni da želite da obrišete člana?") != 0) return;
                    komunikacija.Komunikacija.getInstanca().obrisiClanaDrustva(full);
                    UIHelper.showInfo(pcf, "Sistem je obrisao člana društva.");
                    pripremiFormu();
                    coordinator.Coordinator.getInstanca().osveziGlavnuFormu();
                } catch (Exception ex) {
                    UIHelper.showError(pcf, "Sistem ne može da obriše člana društva.", ex);
                }
            }
        }.execute();
    }

    private void handleAzurirajClan() {
        int red = pcf.getjTableClanovi().getSelectedRow();
        if (red == -1) {
            UIHelper.showError(pcf, "Sistem ne može da učita člana društva.");
            return;
        }
        ModelTabeleClan mtc = (ModelTabeleClan) pcf.getjTableClanovi().getModel();
        ClanDrustva c = mtc.getLista().get(red);

        new SwingWorker<ClanDrustva, Void>() {
            @Override
            protected ClanDrustva doInBackground() throws Exception {
                return komunikacija.Komunikacija.getInstanca().ucitajClanaDrustva(c.getClanID());
            }

            @Override
            protected void done() {
                try {
                    ClanDrustva full = get();
                    if (!isValid(full)) {
                        UIHelper.showError(pcf, "Sistem je učitao člana, ali podaci nisu potpuni.");
                        return;
                    }
                    UIHelper.showInfo(pcf, "Sistem je učitao člana društva.");
                    coordinator.Coordinator.getInstanca().dodajParam("clan", full);
                    coordinator.Coordinator.getInstanca().otvoriIzmeniClanFormu();
                } catch (Exception ex) {
                    UIHelper.showError(pcf, "Sistem ne može da učita člana društva.", ex);
                }
            }
        }.execute();
    }

    private boolean isValid(ClanDrustva c) {
        return c != null && c.getClanID() > 0 && c.getClanIme() != null && !c.getClanIme().trim().isEmpty();
    }

    private void handlePretraga() {
        String ime = pcf.getjTextFieldIme().getText().trim();
        String pol = pcf.getjTextFieldPol().getText().trim();
        String god = pcf.getjTextFieldGod().getText().trim();
        String admin = pcf.getjTextFieldAdmin().getText().trim();
        ModelTabeleClan mtc = (ModelTabeleClan) pcf.getjTableClanovi().getModel();
        mtc.pretrazi(ime, pol, god, admin);

        String msg = mtc.getLista() == null || mtc.getLista().isEmpty()
                ? "Sistem ne može da nađe članove društva po zadatoj vrednosti."
                : "Sistem je našao članove društva po zadatoj vrednosti.";
        UIHelper.showInfo(pcf, msg);
    }

    private void handlePrikazi() {
        int red = pcf.getjTableClanovi().getSelectedRow();
        if (red == -1) {
            UIHelper.showError(pcf, "Sistem ne može da učita člana društva.");
            return;
        }
        ModelTabeleClan mtc = (ModelTabeleClan) pcf.getjTableClanovi().getModel();
        ClanDrustva c = mtc.getLista().get(red);
        try {
            ClanDrustva full = komunikacija.Komunikacija.getInstanca().ucitajClanaDrustva(c.getClanID());
            String info = format(full);
            UIHelper.showInfo(pcf, info + "\n\nSistem je učitao člana društva.", "Detalji člana");
        } catch (Exception ex) {
            UIHelper.showError(pcf, "Sistem ne može da učita člana društva.", ex);
        }
    }

    private String format(ClanDrustva c) {
        if (c == null) {
            return "Nema podataka o članu.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(c.getClanID()).append("\n");
        sb.append("Ime: ").append(c.getClanIme() == null ? "" : c.getClanIme()).append("\n");
        sb.append("Pol: ").append(c.getClanPol() == null ? "" : c.getClanPol()).append("\n");
        sb.append("Godine: ");
        if (c.getClanGod() > 0) {
            sb.append(c.getClanGod());
        }
        sb.append("\n");
        sb.append("Telefon: ").append(c.getClanBrTel() == null ? "" : c.getClanBrTel()).append("\n");
        sb.append("Mejl: ").append(c.getClanEmail() == null ? "" : c.getClanEmail()).append("\n");
        if (c.getAdmin() != null) {
            String adminIme = c.getAdmin().getAdminIme();
            String adminUser = c.getAdmin().getAdminUsername();
            sb.append("Administrator: ");
            boolean wrote = false;
            if (adminIme != null && !adminIme.isEmpty()) {
                sb.append(adminIme);
                wrote = true;
            }
            if (adminUser != null && !adminUser.isEmpty()) {
                if (wrote) sb.append(" ");
                sb.append("(").append(adminUser).append(")");
                wrote = true;
            }
            if (!wrote) {
                sb.append("ID=").append(c.getAdmin().getAdminID());
            }
            sb.append("\n");
        }
        sb.append("Ansambli: ").append(formatAnsambli(c.getClanID())).append("\n");
        return sb.toString();
    }

    private String formatAnsambli(int clanId) {
        List<Ucesce> ucesca = komunikacija.Komunikacija.getInstanca().ucitajUcesca();
        if (ucesca == null || ucesca.isEmpty()) {
            return "Nema podataka.";
        }

        StringBuilder ansambli = new StringBuilder();
        for (Ucesce u : ucesca) {
            if (u == null || u.getClan() == null || u.getClan().getClanID() != clanId || u.getAnsambl() == null) {
                continue;
            }

            if (ansambli.length() > 0) {
                ansambli.append(", ");
            }

            String imeAnsambla = u.getAnsambl().getImeAnsambla();
            if (imeAnsambla != null && !imeAnsambla.trim().isEmpty()) {
                ansambli.append(imeAnsambla.trim());
            } else {
                ansambli.append("ID=").append(u.getAnsambl().getAnsamblID());
            }
        }

        return ansambli.length() == 0 ? "Nema učešća u ansamblima." : ansambli.toString();
    }

    public void otvoriFormu() {
        pripremiFormu();
        pcf.setVisible(true);
    }

    public void pripremiFormu() {
        List<ClanDrustva> clanovi = komunikacija.Komunikacija.getInstanca().ucitajClanove();
        if (clanovi == null) {
            clanovi = new java.util.ArrayList<>();
        }
        ModelTabeleClan mtc = new ModelTabeleClan(clanovi);
        pcf.getjTableClanovi().setModel(mtc);
        podesiSirineKolona();
    }

    public void osveziFormu() {
        pripremiFormu();
    }

    private void podesiSirineKolona() {
        JTable tabela = pcf.getjTableClanovi();
        if (tabela == null || tabela.getColumnModel() == null || tabela.getColumnCount() < 7) {
            return;
        }

        TableColumnModel kolone = tabela.getColumnModel();
        kolone.getColumn(0).setPreferredWidth(50);  // ID
        kolone.getColumn(1).setPreferredWidth(170); // Ime
        kolone.getColumn(2).setPreferredWidth(70);  // Pol
        kolone.getColumn(3).setPreferredWidth(80);  // Godine
        kolone.getColumn(4).setPreferredWidth(140); // Telefon
        kolone.getColumn(5).setPreferredWidth(260); // Mejl
        kolone.getColumn(6).setPreferredWidth(80);  // Admin ID
    }
}
