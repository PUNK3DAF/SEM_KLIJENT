/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import domen.ClanDrustva;
import forme.PrikazClanovaForma;
import forme.model.ModelTabeleClan;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author vldmrk
 */
public class PrikazClanovaController {

    private final PrikazClanovaForma pcf;

    public PrikazClanovaController(PrikazClanovaForma pcf) {
        this.pcf = pcf;
        addActionListeners();
    }

    private void addActionListeners() {

        pcf.addBtnObrisiActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int red = pcf.getjTableClanovi().getSelectedRow();
                if (red == -1) {
                    javax.swing.JOptionPane.showMessageDialog(pcf, "Sistem ne moze da ucita clana drustva.", "Greska", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
                final forme.model.ModelTabeleClan mtc = (forme.model.ModelTabeleClan) pcf.getjTableClanovi().getModel();
                final domen.ClanDrustva c = mtc.getLista().get(red);

                // 1) učitaj člana sa servera u pozadini
                new SwingWorker<domen.ClanDrustva, Void>() {
                    @Override
                    protected domen.ClanDrustva doInBackground() throws Exception {
                        return komunikacija.Komunikacija.getInstanca().ucitajClanaDrustva(c.getClanID());
                    }

                    @Override
                    protected void done() {
                        try {
                            domen.ClanDrustva full = get();
                            // 2) prikazujemo poruku da je učitan (korak 8)
                            javax.swing.JOptionPane.showMessageDialog(pcf, "Sistem je učitao člana društva.", "Informacija", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                            // 3) potvrdimo da korisnik želi brisanje (optional confirm)
                            int izbor = javax.swing.JOptionPane.showConfirmDialog(pcf, "Da li ste sigurni da želite da obrišete člana?", "Potvrda brisanja", javax.swing.JOptionPane.YES_NO_OPTION);
                            if (izbor != javax.swing.JOptionPane.YES_OPTION) {
                                return;
                            }

                            // 4) pozovemo brisanje (može baciti Exception ako server ne dozvoli)
                            try {
                                komunikacija.Komunikacija.getInstanca().obrisiClan(full);
                                javax.swing.JOptionPane.showMessageDialog(pcf, "Sistem je obrisao člana društva.", "Informacija", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                pripremiFormu(); // osveži tabelu
                            } catch (Exception ex) {
                                javax.swing.JOptionPane.showMessageDialog(pcf, "Sistem ne može da obriše člana društva.", "Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            // alternativni scenario 8.1
                            javax.swing.JOptionPane.showMessageDialog(pcf, "Sistem ne može da učita člana društva.", "Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }.execute();
            }
        });

        pcf.addBtnAzurirajActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int red = pcf.getjTableClanovi().getSelectedRow();
                if (red == -1) {
                    javax.swing.JOptionPane.showMessageDialog(pcf, "Sistem ne moze da ucita clana drustva.", "Greska", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
                final forme.model.ModelTabeleClan mtc = (forme.model.ModelTabeleClan) pcf.getjTableClanovi().getModel();
                final domen.ClanDrustva c = mtc.getLista().get(red);

                new SwingWorker<domen.ClanDrustva, Void>() {
                    @Override
                    protected domen.ClanDrustva doInBackground() throws Exception {
                        return komunikacija.Komunikacija.getInstanca().ucitajClanaDrustva(c.getClanID());
                    }

                    @Override
                    protected void done() {
                        try {
                            domen.ClanDrustva full = get();
                            boolean valid = full != null && full.getClanID() > 0
                                    && full.getClanIme() != null && !full.getClanIme().trim().isEmpty();
                            if (!valid) {
                                javax.swing.JOptionPane.showMessageDialog(pcf,
                                        "Sistem je ucitao clana, ali podaci nisu potpuni.",
                                        "Greska",
                                        javax.swing.JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            javax.swing.JOptionPane.showMessageDialog(pcf,
                                    "Sistem je ucitao clana drustva.",
                                    "Informacija",
                                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

                            coordinator.Coordinator.getInstanca().dodajParam("clan", full);
                            coordinator.Coordinator.getInstanca().otvoriIzmeniClanFormu();

                        } catch (Exception ex) {
                            javax.swing.JOptionPane.showMessageDialog(pcf,
                                    "Sistem ne moze da ucita clana drustva.",
                                    "Greska",
                                    javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }.execute();
            }
        });

        pcf.addBtnPretragaActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ime = pcf.getjTextFieldIme().getText().trim();
                String pol = pcf.getjTextFieldPol().getText().trim();
                String god = pcf.getjTextFieldGod().getText().trim();
                String admin = pcf.getjTextFieldAdmin().getText().trim();
                ModelTabeleClan mtc = (ModelTabeleClan) pcf.getjTableClanovi().getModel();
                mtc.pretrazi(ime, pol, god, admin);

                if (mtc.getLista() == null || mtc.getLista().isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(pcf,
                            "Sistem ne moze da nadje clanove drustva po zadatoj vrednosti.",
                            "Informacija",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                } else {
                    javax.swing.JOptionPane.showMessageDialog(pcf,
                            "Sistem je nasao clanove drustva po zadatoj vrednosti.",
                            "Informacija",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        pcf.addBtnPrikaziActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int red = pcf.getjTableClanovi().getSelectedRow();
                if (red == -1) {
                    javax.swing.JOptionPane.showMessageDialog(pcf, "Sistem ne moze da ucita clana drustva.", "Greska", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
                ModelTabeleClan mtc = (ModelTabeleClan) pcf.getjTableClanovi().getModel();
                domen.ClanDrustva c = mtc.getLista().get(red);
                try {
                    domen.ClanDrustva full = komunikacija.Komunikacija.getInstanca().ucitajClanaDrustva(c.getClanID());
                    String info = format(full);
                    javax.swing.JOptionPane.showMessageDialog(pcf,
                            info + "\n\nSistem je ucitao clana drustva.",
                            "Detalji clana",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    javax.swing.JOptionPane.showMessageDialog(pcf,
                            "Sistem ne moze da ucita clana drustva.",
                            "Greska",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    private String format(domen.ClanDrustva c) {
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
                if (wrote) {
                    sb.append(" ");
                }
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
