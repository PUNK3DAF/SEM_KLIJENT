package kontroleri;

import domen.Administrator;
import domen.Ucesce;
import forme.GlavnaForma;
import forme.model.ModelTabeleUcesce;
import java.util.List;

public class GlavnaFormaController {

    private final GlavnaForma gf;

    public GlavnaFormaController(GlavnaForma gf) {
        this.gf = gf;
    }

    public void otvoriFormu() {
        Administrator ulogovani = coordinator.Coordinator.getInstanca().getAdmin();
        gf.getjLabelUlogovani().setText(ulogovani.getAdminIme());
        gf.getjLabelAdminID().setText(String.valueOf(ulogovani.getAdminID()));
        pripremiUcesceTabela();
        gf.setVisible(true);
    }

    private void pripremiUcesceTabela() {
        List<Ucesce> lista = komunikacija.Komunikacija.getInstanca().ucitajUcesca();
        ModelTabeleUcesce mt = new ModelTabeleUcesce(lista);
        gf.getjTableUcesce().setModel(mt);
    }

    public void osveziUcesceTabela() {
        pripremiUcesceTabela();
    }
}
