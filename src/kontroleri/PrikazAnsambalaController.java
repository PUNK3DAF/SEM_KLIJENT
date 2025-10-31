/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import domen.Ansambl;
import forme.PrikazAnsambalaForma;
import forme.model.ModelTabeleAnsambl;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        paf.addBtnObrisiActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int red = paf.getjTableAnsambli().getSelectedRow();
                if (red == -1) {
                    JOptionPane.showMessageDialog(paf, "SISTEM NE MOZE DA OBRISE ANSAMBL", "GRESKA", JOptionPane.ERROR_MESSAGE);
                } else {
                    ModelTabeleAnsambl mta = (ModelTabeleAnsambl) paf.getjTableAnsambli().getModel();
                    Ansambl a = mta.getLista().get(red);
                    try {
                        komunikacija.Komunikacija.getInstanca().obrisiAnsambl(a);
                        JOptionPane.showMessageDialog(paf, "SISTEM JE USPESNO OBRISAO ANSAMBL", "USPEH", JOptionPane.INFORMATION_MESSAGE);
                        pripremiFormu();
                    } catch (HeadlessException ex) {
                        JOptionPane.showMessageDialog(paf, "SISTEM NE MOZE DA OBRISE ANSAMBL", "GRESKA", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        paf.addBtnAzurirajActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int red = paf.getjTableAnsambli().getSelectedRow();
                if (red == -1) {
                    JOptionPane.showMessageDialog(paf, "SISTEM NE MOZE DA AZURIRA ANSAMBL", "GRESKA", JOptionPane.ERROR_MESSAGE);
                } else {
                    ModelTabeleAnsambl mta = (ModelTabeleAnsambl) paf.getjTableAnsambli().getModel();
                    Ansambl a = mta.getLista().get(red);
                    coordinator.Coordinator.getInstanca().dodajParam("Ansambl", a);
                    coordinator.Coordinator.getInstanca().otvoriIzmeniAnsamblFormu();
                }
            }
        });
        paf.addBtnPretragaActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ime = paf.getjTextFieldIme().getText().trim();
                String opis = paf.getjTextFieldOpis().getText().trim();
                String admin = paf.getjTextFieldAdmin().getText().trim();
                ModelTabeleAnsambl mta = (ModelTabeleAnsambl) paf.getjTableAnsambli().getModel();
                mta.pretrazi(ime, opis, admin);
            }
        });
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
