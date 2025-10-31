/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import coordinator.Coordinator;
import domen.ClanDrustva;
import domen.Ucesce;
import forme.UpravljajClanovimaForma;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vldmrk
 */
public class UpravljajClanovimaController {

    private final UpravljajClanovimaForma dlg;

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

        // determine pre-selected items:
        List<ClanDrustva> preSelected = new ArrayList<>();
        Object selObj = Coordinator.getInstanca().vratiParam("izabraniClanovi");
        if (selObj instanceof List) {
            List<?> tmp = (List<?>) selObj;
            for (Object o : tmp) {
                if (o instanceof ClanDrustva) {
                    preSelected.add((ClanDrustva) o);
                }
            }
        } else {
            Object a = Coordinator.getInstanca().vratiParam("Ansambl");
            if (a != null && a instanceof domen.Ansambl) {
                domen.Ansambl ans = (domen.Ansambl) a;
                if (ans.getUcesca() != null) {
                    for (Ucesce u : ans.getUcesca()) {
                        if (u.getClan() != null) {
                            preSelected.add(u.getClan());
                        }
                    }
                }
            }
        }

        List<ClanDrustva> available = new ArrayList<>();
        for (ClanDrustva c : all) {
            boolean found = false;
            for (ClanDrustva ps : preSelected) {
                if (ps.getClanID() == c.getClanID()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                available.add(c);
            }
        }
        dlg.setDostupni(available);
        dlg.setOdabrani(preSelected);

        // wire buttons
        dlg.getjButtonDodaj().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveSelected(dlg.getjListDostupni(), dlg.getjListOdabrani());
            }
        });

        dlg.getjButtonObrisi().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveSelected(dlg.getjListOdabrani(), dlg.getjListDostupni());
            }
        });

        dlg.getjButtonSacuvaj().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<ClanDrustva> sel = dlg.odabraniLista();
                Coordinator.getInstanca().dodajParam("izabraniClanovi", sel);
                dlg.dispose();
            }
        });
        dlg.getjButtonIzadji().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dlg.dispose();
            }
        });
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

    public void open() {
        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }
}
