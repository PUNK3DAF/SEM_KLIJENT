package kontroleri;

import domen.Zanr;
import forme.UIHelper;
import forme.UpravljajZanrovimaForma;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class UpravljajZanrovimaController {

    private final UpravljajZanrovimaForma forma;

    public UpravljajZanrovimaController(UpravljajZanrovimaForma forma) {
        this.forma = forma;
        init();
    }

    private void init() {
        ucitajZanrove();
        
        forma.getjButtonDodaj().addActionListener(e -> handleDodaj());
        forma.getjButtonZatvori().addActionListener(e -> forma.dispose());
    }

    private void ucitajZanrove() {
        List<Zanr> zanrovi = new ArrayList<>();
        try {
            zanrovi = komunikacija.Komunikacija.getInstanca().ucitajZanrove();
        } catch (Exception ex) {
            UIHelper.showError(forma, "Greska pri ucitavanju zanrova", ex);
        }
        forma.setZanrovi(zanrovi);
    }

    private void handleDodaj() {
        String naziv = JOptionPane.showInputDialog(forma, "Unesite naziv zanra:", "Dodaj zanr", JOptionPane.PLAIN_MESSAGE);
        if (naziv == null || naziv.trim().isEmpty()) {
            UIHelper.showError(forma, "Sistem ne moze da kreira zanr\nRazlog: Naziv ne sme biti prazan");
            return;
        }
        
        Zanr z = new Zanr();
        z.setNaziv(naziv.trim());
        
        try {
            komunikacija.Komunikacija.getInstanca().kreirajZanr(z);
            UIHelper.showInfo(forma, "Sistem je kreirao zanr", "Uspeh");
            ucitajZanrove();
        } catch (Exception ex) {
            UIHelper.showError(forma, "Sistem ne moze da kreira zanr", ex);
        }
    }

    public void open() {
        forma.setLocationRelativeTo(null);
        forma.setVisible(true);
    }
}
