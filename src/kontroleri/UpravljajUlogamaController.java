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
        forma.getjButtonIzmeni().addActionListener(e -> handleIzmeni());
        forma.getjButtonObrisi().addActionListener(e -> handleObrisi());
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
            JOptionPane.showMessageDialog(forma, "Naziv uloge ne sme biti prazan!", "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Uloga u = new Uloga();
        u.setNaziv(naziv.trim());

        try {
            komunikacija.Komunikacija.getInstanca().kreirajUlogu(u);
            JOptionPane.showMessageDialog(forma, "Uloga kreirana!", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            ucitajUloge();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greška: " + ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleIzmeni() {
        int row = forma.getjTableUloge().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(forma, "Morate selektovati ulogu!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Uloga u = forma.getIzabranaUloga();
        if (u == null) {
            return;
        }

        String naziv = JOptionPane.showInputDialog(forma, "Unesite novi naziv uloge:", "Izmeni ulogu", JOptionPane.PLAIN_MESSAGE);
        if (naziv == null || naziv.trim().isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Naziv uloge ne sme biti prazan!", "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }

        u.setNaziv(naziv.trim());

        try {
            komunikacija.Komunikacija.getInstanca().izmeniUlogu(u);
            JOptionPane.showMessageDialog(forma, "Uloga izmenjena!", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            ucitajUloge();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greška: " + ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleObrisi() {
        int row = forma.getjTableUloge().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(forma, "Morate selektovati ulogu!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Uloga u = forma.getIzabranaUloga();
        if (u == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(forma, "Da li ste sigurni da želite da obrišete ulogu '" + u.getNaziv() + "'?", "Potvrda", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            komunikacija.Komunikacija.getInstanca().obrisiUlogu(u);
            JOptionPane.showMessageDialog(forma, "Uloga obrisana!", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            ucitajUloge();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greška: " + ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void open() {
        forma.setLocationRelativeTo(null);
        forma.setVisible(true);
    }
}
