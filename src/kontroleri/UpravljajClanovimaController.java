package kontroleri;

import coordinator.Coordinator;
import domen.ClanDrustva;
import domen.Ucesce;
import forme.UpravljajClanovimaForma;
import java.util.ArrayList;
import java.util.List;

public class UpravljajClanovimaController {

    private final UpravljajClanovimaForma dlg;
    private List<ClanDrustva> preOdabrani;

    public UpravljajClanovimaController(UpravljajClanovimaForma dlg) {
        this.dlg = dlg;
        init();
    }

    private void init() {
        // load all clanovi from server
        List<ClanDrustva> all = new ArrayList<>();
        try {
            all = komunikacija.Komunikacija.getInstanca().ucitajClanove();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // determine pre-selected items (members only; roles handled on save)
        preOdabrani = new ArrayList<>();
        Object selObj = Coordinator.getInstanca().vratiParam("izabraniClanovi");
        if (selObj instanceof List) {
            List<?> tmp = (List<?>) selObj;
            for (Object o : tmp) {
                if (o instanceof ClanDrustva) {
                    preOdabrani.add((ClanDrustva) o);
                }
            }
        } else {
            Object a = Coordinator.getInstanca().vratiParam("Ansambl");
            if (a != null && a instanceof domen.Ansambl) {
                domen.Ansambl ans = (domen.Ansambl) a;
                if (ans.getUcesca() != null) {
                    for (Ucesce u : ans.getUcesca()) {
                        if (u.getClan() != null) {
                            preOdabrani.add(u.getClan());
                        }
                    }
                }
            }
        }

        List<ClanDrustva> available = new ArrayList<>();
        for (ClanDrustva c : all) {
            boolean found = false;
            for (ClanDrustva sel : preOdabrani) {
                if (sel.getClanID() == c.getClanID()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                available.add(c);
            }
        }
        dlg.setDostupni(available);
        dlg.setOdabrani(preOdabrani);

        dlg.getjButtonDodaj().addActionListener(e -> moveSelected(dlg.getjListDostupni(), dlg.getjListOdabrani()));
        dlg.getjButtonObrisi().addActionListener(e -> moveSelected(dlg.getjListOdabrani(), dlg.getjListDostupni()));
        dlg.getjButtonSacuvaj().addActionListener(e -> handleSacuvaj());
        dlg.getjButtonIzadji().addActionListener(e -> dlg.dispose());
    }

    private void moveSelected(javax.swing.JList<ClanDrustva> from, javax.swing.JList<ClanDrustva> to) {
        javax.swing.DefaultListModel<ClanDrustva> fm = (javax.swing.DefaultListModel<ClanDrustva>) from.getModel();
        javax.swing.DefaultListModel<ClanDrustva> tm = (javax.swing.DefaultListModel<ClanDrustva>) to.getModel();
        List<ClanDrustva> toMove = new ArrayList<>();
        for (Object o : from.getSelectedValuesList()) {
            if (o instanceof ClanDrustva) {
                toMove.add((ClanDrustva) o);
            }
        }
        for (ClanDrustva c : toMove) {
            for (int i = 0; i < fm.size(); i++) {
                if (fm.get(i).getClanID() == c.getClanID()) {
                    fm.remove(i);
                    break;
                }
            }
            tm.addElement(c);
        }
        from.clearSelection();
        to.clearSelection();
    }

    private void handleSacuvaj() {
        // Jednostavan pristup: za svakog odabranog clana pitaj ulogu i sacuvaj u mapu
        List<ClanDrustva> sel = dlg.odabraniLista();
        java.util.Map<Integer, String> ulogeMap = new java.util.HashMap<>();
        for (ClanDrustva c : sel) {
            String init = "Clan";
            String entered;
            while (true) {
                entered = javax.swing.JOptionPane.showInputDialog(
                        dlg,
                        "Unesite ulogu za člana " + c.getClanIme() + ":",
                        init
                );
                if (entered == null) { // korisnik kliknuo Cancel, zadrži default
                    entered = init;
                    break;
                }
                entered = entered.trim();
                if (entered.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(
                            dlg,
                            "Uloga ne sme biti prazna.",
                            "Greška",
                            javax.swing.JOptionPane.ERROR_MESSAGE
                    );
                    continue; // ponovo pitaj
                }
                break; // validna uloga
            }
            ulogeMap.put(c.getClanID(), entered);
        }

        Coordinator.getInstanca().dodajParam("izabraniClanovi", sel);
        Coordinator.getInstanca().dodajParam("ulogeClanova", ulogeMap);
        dlg.dispose();
    }

    public void open() {
        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }
}
