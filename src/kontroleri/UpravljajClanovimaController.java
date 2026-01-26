package kontroleri;

import coordinator.Coordinator;
import domen.ClanDrustva;
import domen.Ucesce;
import domen.Uloga;
import forme.UpravljajClanovimaForma;
import java.util.ArrayList;
import java.util.List;

public class UpravljajClanovimaController {

    private final UpravljajClanovimaForma dlg;
    private List<ClanDrustva> preOdabrani;
    private List<Uloga> uloge;

    public UpravljajClanovimaController(UpravljajClanovimaForma dlg) {
        this.dlg = dlg;
        init();
    }

    private void init() {
        List<ClanDrustva> all = new ArrayList<>();
        uloge = new ArrayList<>();
        try {
            all = komunikacija.Komunikacija.getInstanca().ucitajClanove();
            uloge = komunikacija.Komunikacija.getInstanca().ucitajUloge();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

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
        List<ClanDrustva> sel = dlg.odabraniLista();
        java.util.Map<Integer, String> ulogeMap = new java.util.HashMap<>();
        for (ClanDrustva c : sel) {
            String defaultVal = "Clan";
            Object[] options;
            if (uloge != null && !uloge.isEmpty()) {
                options = uloge.stream().map(Uloga::getNaziv).toArray();
                // ako ima prethodno izabrano, koristi ga
                    Object prevObj = coordinator.Coordinator.getInstanca().vratiParam("ulogeClanova");
                    if (prevObj instanceof java.util.Map) {
                        java.util.Map<?, ?> map = (java.util.Map<?, ?>) prevObj;
                        Object prev = map.get(c.getClanID());
                        if (prev instanceof String) {
                            defaultVal = (String) prev;
                        }
                    }
            } else {
                options = new Object[]{defaultVal};
            }

            javax.swing.JComboBox<Object> combo = new javax.swing.JComboBox<>(options);
            combo.setSelectedItem(defaultVal);
            int res = javax.swing.JOptionPane.showConfirmDialog(
                    dlg,
                    combo,
                    "Odaberite ulogu za člana " + c.getClanIme(),
                    javax.swing.JOptionPane.OK_CANCEL_OPTION,
                    javax.swing.JOptionPane.PLAIN_MESSAGE
            );
            String chosen = defaultVal;
            if (res == javax.swing.JOptionPane.OK_OPTION && combo.getSelectedItem() != null) {
                chosen = combo.getSelectedItem().toString().trim();
            }
            if (chosen.isEmpty()) {
                chosen = defaultVal;
            }
            ulogeMap.put(c.getClanID(), chosen);
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
