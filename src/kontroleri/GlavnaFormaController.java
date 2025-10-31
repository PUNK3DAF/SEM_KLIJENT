/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import domen.Administrator;
import domen.Ucesce;
import forme.GlavnaForma;
import forme.model.ModelTabeleUcesce;
import java.util.List;

/**
 *
 * @author vldmrk
 */
public class GlavnaFormaController {

    private final GlavnaForma gf;

    public GlavnaFormaController(GlavnaForma gf) {
        this.gf = gf;
        addActionListeners();
    }

    private void addActionListeners() {

    }

    public void otvoriFormu() {
        Administrator ulogovani = coordinator.Coordinator.getInstanca().getAdmin();
        String ime = ulogovani.getAdminIme();
        gf.setVisible(true);
        gf.getjLabelUlogovani().setText(ime);
        gf.getjLabelAdminID().setText(String.valueOf(ulogovani.getAdminID()));
        pripremiUcesceTabela();
    }

    private void pripremiUcesceTabela() {
        List<Ucesce> lista = komunikacija.Komunikacija.getInstanca().ucitajUcesca();
        ModelTabeleUcesce mt = new ModelTabeleUcesce(lista);
        gf.getjTableUcesce().setModel(mt);
    }

    public void osveziUcesceTabela() {
        try {
            List<domen.Ucesce> lista = komunikacija.Komunikacija.getInstanca().ucitajUcesca();
            ModelTabeleUcesce mt = new ModelTabeleUcesce(lista);
            gf.getjTableUcesce().setModel(mt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
