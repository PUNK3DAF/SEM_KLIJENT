package kontroleri;

import domen.Zanr;
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
        forma.getjButtonIzmeni().addActionListener(e -> handleIzmeni());
        forma.getjButtonObrisi().addActionListener(e -> handleObrisi());
        forma.getjButtonZatvori().addActionListener(e -> forma.dispose());
    }

    private void ucitajZanrove() {
        List<Zanr> zanrovi = new ArrayList<>();
        try {
            zanrovi = komunikacija.Komunikacija.getInstanca().ucitajZanrove();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        forma.setZanrovi(zanrovi);
    }

    private void handleDodaj() {
        String naziv = JOptionPane.showInputDialog(forma, "Unesite naziv žanra:", "Dodaj žanr", JOptionPane.PLAIN_MESSAGE);
        if (naziv == null || naziv.trim().isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Naziv žanra ne sme biti prazan!", "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Zanr z = new Zanr();
        z.setNaziv(naziv.trim());
        
        try {
            komunikacija.Komunikacija.getInstanca().kreirajZanr(z);
            JOptionPane.showMessageDialog(forma, "Žanr kreiran!", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            ucitajZanrove();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greška: " + ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleIzmeni() {
        int row = forma.getjTableZanrovi().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(forma, "Morate selektovati žanr!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Zanr z = forma.getIzabraniZanr();
        if (z == null) {
            return;
        }
        
        String naziv = JOptionPane.showInputDialog(forma, "Unesite novi naziv žanra:", "Izmeni žanr", JOptionPane.PLAIN_MESSAGE);
        if (naziv == null || naziv.trim().isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Naziv žanra ne sme biti prazan!", "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        z.setNaziv(naziv.trim());
        
        try {
            komunikacija.Komunikacija.getInstanca().izmeniZanr(z);
            JOptionPane.showMessageDialog(forma, "Žanr izmenjen!", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            ucitajZanrove();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greška: " + ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleObrisi() {
        int row = forma.getjTableZanrovi().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(forma, "Morate selektovati žanr!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Zanr z = forma.getIzabraniZanr();
        if (z == null) {
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(forma, "Da li ste sigurni da želite da obrišete žanr '" + z.getNaziv() + "'?", "Potvrda", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        try {
            komunikacija.Komunikacija.getInstanca().obrisiZanr(z);
            JOptionPane.showMessageDialog(forma, "Žanr obrisan!", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            ucitajZanrove();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greška: " + ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void open() {
        forma.setLocationRelativeTo(null);
        forma.setVisible(true);
    }
}
