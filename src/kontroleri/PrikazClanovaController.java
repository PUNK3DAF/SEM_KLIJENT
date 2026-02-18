package kontroleri;

import domen.ClanDrustva;
import forme.PrikazClanovaForma;
import forme.UIHelper;
import forme.model.ModelTabeleClan;
import java.util.List;
import javax.swing.SwingWorker;

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
            UIHelper.showError(pcf, "Sistem ne moze da ucita clana drustva.");
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
                    UIHelper.showInfo(pcf, "Sistem je ucitao clana drustva.");
                    if (UIHelper.confirm(pcf, "Da li ste sigurni da zelite da obrisete clana?") != 0) return;
                    komunikacija.Komunikacija.getInstanca().obrisiClanaDrustva(full);
                    UIHelper.showInfo(pcf, "Sistem je obrisao clana drustva.");
                    pripremiFormu();
                    coordinator.Coordinator.getInstanca().osveziGlavnuFormu();
                } catch (Exception ex) {
                    UIHelper.showError(pcf, "Sistem ne moze da obrise clana drustva.", ex);
                }
            }
        }.execute();
    }

    private void handleAzurirajClan() {
        int red = pcf.getjTableClanovi().getSelectedRow();
        if (red == -1) {
            UIHelper.showError(pcf, "Sistem ne moce da ucita clana drustva.");
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
                        UIHelper.showError(pcf, "Sistem je ucitao clana, ali podaci nisu potpuni.");
                        return;
                    }
                    UIHelper.showInfo(pcf, "Sistem je ucitao clana drustva.");
                    coordinator.Coordinator.getInstanca().dodajParam("clan", full);
                    coordinator.Coordinator.getInstanca().otvoriIzmeniClanFormu();
                } catch (Exception ex) {
                    UIHelper.showError(pcf, "Sistem ne moze da ucita clana drustva.", ex);
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
                ? "Sistem ne moze da nadje clanove drustva po zadatoj vrednosti."
                : "Sistem je nasao clanove drustva po zadatoj vrednosti.";
        UIHelper.showInfo(pcf, msg);
    }

    private void handlePrikazi() {
        int red = pcf.getjTableClanovi().getSelectedRow();
        if (red == -1) {
            UIHelper.showError(pcf, "Sistem ne moze da ucita clana drustva.");
            return;
        }
        ModelTabeleClan mtc = (ModelTabeleClan) pcf.getjTableClanovi().getModel();
        ClanDrustva c = mtc.getLista().get(red);
        try {
            ClanDrustva full = komunikacija.Komunikacija.getInstanca().ucitajClanaDrustva(c.getClanID());
            String info = format(full);
            UIHelper.showInfo(pcf, info + "\n\nSistem je ucitao clana drustva.", "Detalji clana");
        } catch (Exception ex) {
            UIHelper.showError(pcf, "Sistem ne moze da ucita clana drustva.", ex);
        }
    }

    private String format(ClanDrustva c) {
        if (c == null) {
            return "Nema podataka o clanu.";
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
        return sb.toString();
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
    }

    public void osveziFormu() {
        pripremiFormu();
    }
}
