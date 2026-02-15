package kontroleri;

import domen.Uloga;
import forme.UpravljajUlogamaForma;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class UpravljajUlogamaController {

    private final UpravljajUlogamaForma forma;

    public UpravljajUlogamaController(UpravljajUlogamaForma forma) {
        this.forma = forma;
        init();
    }

    private void init() {
        ucitajUloge();

        forma.getjButtonDodaj().addActionListener(e -> handleDodaj());
        forma.getjButtonZatvori().addActionListener(e -> forma.dispose());
    }

    private void ucitajUloge() {
        List<Uloga> uloge = new ArrayList<>();
        try {
            uloge = komunikacija.Komunikacija.getInstanca().ucitajUloge();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        forma.setUloge(uloge);
    }

    private void handleDodaj() {
        String naziv = JOptionPane.showInputDialog(forma, "Unesite naziv uloge:", "Dodaj ulogu", JOptionPane.PLAIN_MESSAGE);
        if (naziv == null || naziv.trim().isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Naziv uloge ne sme biti prazan!", "Greska", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Uloga u = new Uloga();
        u.setNaziv(naziv.trim());

        try {
            komunikacija.Komunikacija.getInstanca().kreirajUlogu(u);
            JOptionPane.showMessageDialog(forma, "Sistem je kreirao ulogu", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            ucitajUloge();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Sistem ne moze da kreira ulogu", "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void open() {
        forma.setLocationRelativeTo(null);
        forma.setVisible(true);
    }
}
