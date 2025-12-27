package kontroleri;

import coordinator.Coordinator;
import domen.ClanDrustva;
import domen.Ucesce;
import forme.UpravljajClanovimaForma;
import forme.model.ClanSaUlogom;
import java.util.ArrayList;
import java.util.List;

public class UpravljajClanovimaController {

    private final UpravljajClanovimaForma dlg;
    private List<ClanSaUlogom> clanoviSaUlogom;

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

        // determine pre-selected items sa ulogama:
        clanoviSaUlogom = new ArrayList<>();
        Object selObj = Coordinator.getInstanca().vratiParam("izabraniClanovi");
        if (selObj instanceof List) {
            List<?> tmp = (List<?>) selObj;
            for (Object o : tmp) {
                if (o instanceof ClanSaUlogom) {
                    clanoviSaUlogom.add((ClanSaUlogom) o);
                } else if (o instanceof ClanDrustva) {
                    clanoviSaUlogom.add(new ClanSaUlogom((ClanDrustva) o, "Clan"));
                }
            }
        } else {
            Object a = Coordinator.getInstanca().vratiParam("Ansambl");
            if (a != null && a instanceof domen.Ansambl) {
                domen.Ansambl ans = (domen.Ansambl) a;
                if (ans.getUcesca() != null) {
                    for (Ucesce u : ans.getUcesca()) {
                        if (u.getClan() != null) {
                            clanoviSaUlogom.add(new ClanSaUlogom(u.getClan(), u.getUloga() != null ? u.getUloga() : "Clan"));
                        }
                    }
                }
            }
        }

        List<ClanDrustva> available = new ArrayList<>();
        for (ClanDrustva c : all) {
            boolean found = false;
            for (ClanSaUlogom cu : clanoviSaUlogom) {
                if (cu.getClan().getClanID() == c.getClanID()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                available.add(c);
            }
        }
        dlg.setDostupni(available);
        dlg.setOdabraniSaUlogom(clanoviSaUlogom);

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
            
            // Ako se dodaje u "odabrani", dodaj u clanoviSaUlogom sa default ulogom
            if (to == dlg.getjListOdabrani()) {
                clanoviSaUlogom.add(new ClanSaUlogom(c, "Clan"));
            } else {
                // Ako se briše iz "odabrani", ukloni iz clanoviSaUlogom
                clanoviSaUlogom.removeIf(cu -> cu.getClan().getClanID() == c.getClanID());
            }
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
            String entered = javax.swing.JOptionPane.showInputDialog(
                    dlg,
                    "Unesite ulogu za clana (ID=" + c.getClanID() + "):",
                    init
            );
            if (entered == null) entered = init;
            entered = entered.trim();
            if (entered.isEmpty()) entered = init;
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
