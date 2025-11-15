/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import domen.ClanDrustva;
import forme.DodajClanForma;
import forme.FormaMod;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author vldmrk
 */
public class DodajClanController {

    private final DodajClanForma dcf;

    public DodajClanController(DodajClanForma dcf) {
        this.dcf = dcf;
        addActionListeners();
    }

    private void addActionListeners() {
        dcf.addDodajActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dodaj(e);
            }

            private void dodaj(ActionEvent e) {
                String ime = dcf.getjTextFieldIme().getText().trim();
                String pol = dcf.getjTextFieldPol().getText().trim();
                String godS = dcf.getjTextFieldGod().getText().trim();
                String tel = dcf.getjTextFieldTel().getText().trim();
                int god = 0;
                try {
                    if (!godS.isEmpty()) {
                        god = Integer.parseInt(godS);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dcf, "Godine moraju biti broj", "GRESKA", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                domen.Administrator admin = coordinator.Coordinator.getInstanca().getAdmin();
                ClanDrustva c = new ClanDrustva(-1, ime, pol, god, tel, admin);
                try {
                    komunikacija.Komunikacija.getInstanca().dodajClan(c);
                    JOptionPane.showMessageDialog(dcf, "Sistem je kreirao clana drustva", "USPEH", JOptionPane.INFORMATION_MESSAGE);
                    dcf.dispose();

                    coordinator.Coordinator.getInstanca().osveziClanFormu();
                } catch (Exception ex) {
                    String razlog = ex.getMessage() == null ? "" : ex.getMessage();
                    String poruka = "Sistem ne moze da kreira clana drustva";
                    if (!razlog.isEmpty()) {
                        poruka += "\nRazlog: " + razlog;
                    }
                    JOptionPane.showMessageDialog(dcf, poruka, "GRESKA", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        );

        dcf.addAzurirajActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                azuriraj(e);
            }

            private void azuriraj(ActionEvent e) {
                String ime = dcf.getjTextFieldIme().getText().trim();
                String pol = dcf.getjTextFieldPol().getText().trim();
                String godS = dcf.getjTextFieldGod().getText().trim();
                String tel = dcf.getjTextFieldTel().getText().trim();
                int god = 0;
                try {
                    if (!godS.isEmpty()) {
                        god = Integer.parseInt(godS);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dcf, "Godine moraju biti broj", "GRESKA", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                ClanDrustva original = (ClanDrustva) coordinator.Coordinator.getInstanca().vratiParam("clan");
                if (original == null) {
                    JOptionPane.showMessageDialog(dcf, "Originalni clan nije dostupan za izmenu", "GRESKA", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                original.setClanIme(ime);
                original.setClanPol(pol);
                original.setClanGod(god);
                original.setClanBrTel(tel);

                try {
                    komunikacija.Komunikacija.getInstanca().azurirajClan(original);
                    JOptionPane.showMessageDialog(dcf, "Sistem je zapamtio clana drustva", "USPEH", JOptionPane.INFORMATION_MESSAGE);
                    dcf.dispose();
                    coordinator.Coordinator.getInstanca().osveziClanFormu();
                } catch (Exception ex) {
                    String poruka = ex.getMessage() == null ? "Sistem ne moze da zapamti clana drustva" : ex.getMessage();
                    JOptionPane.showMessageDialog(dcf, poruka, "GRESKA", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void otvoriFormu(FormaMod fm) {
        pripremiFormu(fm);
        dcf.setVisible(true);
    }

    private void pripremiFormu(FormaMod fm) {
        switch (fm) {
            case DODAJ:
                dcf.getjButtonAzuriraj().setVisible(false);
                dcf.getjButtonDodaj().setVisible(true);
                dcf.getjButtonDodaj().setEnabled(true);
                break;
            case IZMENI:
                dcf.getjButtonDodaj().setVisible(false);
                dcf.getjButtonAzuriraj().setVisible(true);
                dcf.getjButtonAzuriraj().setEnabled(true);
                ClanDrustva c = (ClanDrustva) coordinator.Coordinator.getInstanca().vratiParam("clan");
                if (c != null) {
                    dcf.getjTextFieldIme().setText(c.getClanIme());
                    dcf.getjTextFieldPol().setText(c.getClanPol());
                    dcf.getjTextFieldGod().setText(String.valueOf(c.getClanGod()));
                    dcf.getjTextFieldTel().setText(c.getClanBrTel());
                }
                break;
            default:
                throw new AssertionError();
        }
    }
}
