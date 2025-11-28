/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import domen.Ansambl;
import forme.DodajAnsamblForma;
import forme.FormaMod;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author vldmrk
 */
public class DodajAnsamblController {

    private final DodajAnsamblForma daf;

    public DodajAnsamblController(DodajAnsamblForma daf) {
        this.daf = daf;
        addActionListeners();
    }

    private void addActionListeners() {
        daf.DodajAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dodaj(e);
            }

            private void dodaj(ActionEvent e) {
                String ime = daf.getjTextFieldImeAns().getText().trim();
                String opis = daf.getjTextAreaOpisAns().getText().trim();

                domen.Administrator admin = coordinator.Coordinator.getInstanca().getAdmin();
                domen.Ansambl a = new domen.Ansambl(-1, ime, opis, admin);

                List<domen.Ucesce> ucesca = ucescaIzCoordinatora();
                if (ucesca != null) {
                    a.setUcesca(ucesca);
                }

                // pripremni debug i izabraniClanovi (moze da ostane pre try-a)
                Object sel = coordinator.Coordinator.getInstanca().vratiParam("izabraniClanovi");
                System.out.println("DEBUG: izabraniClanovi = " + sel);
                if (sel instanceof java.util.List) {
                    java.util.List<?> tmp = (java.util.List<?>) sel;
                    System.out.println("DEBUG: izabraniClanovi.size = " + tmp.size());
                    for (Object o : tmp) {
                        if (o instanceof domen.ClanDrustva) {
                            domen.ClanDrustva c = (domen.ClanDrustva) o;
                            System.out.println("DEBUG: clan id=" + c.getClanID() + " ime=" + c.getClanIme());
                        } else {
                            System.out.println("DEBUG: element klase " + o.getClass().getName() + " -> " + o);
                        }
                    }
                } else {
                    System.out.println("DEBUG: izabraniClanovi nije List ili je null");
                }

                try {
                    komunikacija.Komunikacija.getInstanca().dodajAnsamblSaSastavom(a);
                    // poruka po specifikaciji
                    JOptionPane.showMessageDialog(daf, "Sistem je kreirao ansambl", "USPEH", JOptionPane.INFORMATION_MESSAGE);
                    daf.dispose();
                    coordinator.Coordinator.getInstanca().osveziGlavnuFormu();
                } catch (Exception ex) {
                    String razlog = ex.getMessage() == null ? "" : ex.getMessage();
                    String poruka = "Sistem ne moze da kreira ansambl";
                    if (!razlog.isEmpty()) {
                        poruka += "\nRazlog: " + razlog;
                    }
                    JOptionPane.showMessageDialog(daf, poruka, "GRESKA", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        daf.AzurirajAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                azuriraj(e);
            }

            private void azuriraj(ActionEvent e) {
                String ime = daf.getjTextFieldImeAns().getText().trim();
                String opis = daf.getjTextAreaOpisAns().getText().trim();

                domen.Ansambl original = (domen.Ansambl) coordinator.Coordinator.getInstanca().vratiParam("Ansambl");
                if (original == null) {
                    JOptionPane.showMessageDialog(daf, "Originalni ansambl nije dostupan", "GRESKA", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                original.setImeAnsambla(ime);
                original.setOpisAnsambla(opis);

                List<domen.Ucesce> ucesca = ucescaIzCoordinatora();
                original.setUcesca(ucesca);

                try {
                    // send update to server (may throw Exception with server reason)
                    komunikacija.Komunikacija.getInstanca().azurirajSastavAnsambla(original);
                    // success message per spec
                    JOptionPane.showMessageDialog(daf, "Sistem je zapamtio ansambl", "USPEH", JOptionPane.INFORMATION_MESSAGE);
                    daf.dispose();
                    coordinator.Coordinator.getInstanca().osveziFormu();
                } catch (Exception ex) {
                    // show spec-styled error with server reason
                    String razlog = ex.getMessage() == null ? "" : ex.getMessage();
                    String poruka = "Sistem ne moze da zapamti ansambl";
                    if (!razlog.isEmpty()) {
                        poruka += "\nRazlog: " + razlog;
                    }
                    JOptionPane.showMessageDialog(daf, poruka, "GRESKA", JOptionPane.ERROR_MESSAGE);

                    // attempt to reload fresh ansambl from server and repopulate the form (like clan flow)
                    final domen.Ansambl savedOriginal = (domen.Ansambl) coordinator.Coordinator.getInstanca().vratiParam("Ansambl");
                    if (savedOriginal != null && savedOriginal.getAnsamblID() > 0) {
                        new javax.swing.SwingWorker<domen.Ansambl, Void>() {
                            @Override
                            protected domen.Ansambl doInBackground() throws Exception {
                                return komunikacija.Komunikacija.getInstanca().ucitajAnsambl(savedOriginal.getAnsamblID());
                            }

                            @Override
                            protected void done() {
                                try {
                                    domen.Ansambl fresh = get();
                                    if (fresh != null) {
                                        coordinator.Coordinator.getInstanca().dodajParam("Ansambl", fresh);
                                        daf.getjTextFieldImeAns().setText(fresh.getImeAnsambla() == null ? "" : fresh.getImeAnsambla());
                                        daf.getjTextAreaOpisAns().setText(fresh.getOpisAnsambla() == null ? "" : fresh.getOpisAnsambla());
                                        // if you also track selected members in coordinator, consider repopulating that UI as well
                                    } else {
                                        // ansambl no longer exists -> clear fields
                                        daf.getjTextFieldImeAns().setText("");
                                        daf.getjTextAreaOpisAns().setText("");
                                    }
                                } catch (Exception ex2) {
                                    // silent fail (optionally log)
                                }
                            }
                        }.execute();
                    }
                }
            }
        });
        daf.UpravljajClanovimaAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forme.UpravljajClanovimaForma dlg = new forme.UpravljajClanovimaForma(daf, true);
                new UpravljajClanovimaController(dlg).open();
            }
        });
    }

    private List<domen.Ucesce> ucescaIzCoordinatora() {
        Object obj = coordinator.Coordinator.getInstanca().vratiParam("izabraniClanovi");
        if (obj == null) {
            return null;
        }
        List<domen.ClanDrustva> selected = (List<domen.ClanDrustva>) obj;
        if (selected.isEmpty()) {
            return null;
        }
        List<domen.Ucesce> res = new java.util.ArrayList<>();
        for (domen.ClanDrustva c : selected) {
            domen.Ucesce u = new domen.Ucesce();
            u.setClan(c);
            u.setUloga("Clan");
            res.add(u);
        }
        return res;
    }

    public void otvoriFormu(FormaMod fm) {
        pripremiFormu(fm);
        daf.setVisible(true);
    }

    private void pripremiFormu(FormaMod fm) {
        switch (fm) {
            case DODAJ:
                daf.getjButtonAzuriraj().setVisible(false);
                daf.getjButtonDodaj().setVisible(true);
                daf.getjButtonDodaj().setEnabled(true);
                break;
            case IZMENI:
                daf.getjButtonDodaj().setVisible(false);
                daf.getjButtonAzuriraj().setVisible(true);
                daf.getjButtonAzuriraj().setEnabled(true);
                Ansambl a = (Ansambl) coordinator.Coordinator.getInstanca().vratiParam("Ansambl");
                daf.getjTextFieldImeAns().setText(a.getImeAnsambla());
                daf.getjTextAreaOpisAns().setText(a.getOpisAnsambla());
                break;
            default:
                throw new AssertionError();
        }
    }

}
