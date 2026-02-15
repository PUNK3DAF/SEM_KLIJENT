/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import domen.Mesto;
import forme.UIHelper;
import forme.UpravljajMestimaForma;
import forme.model.MestoTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import komunikacija.Komunikacija;

/**
 *
 * @author vldmrk
 */
public class UpravljajMestimaController {

    private UpravljajMestimaForma forma;

    public UpravljajMestimaController(UpravljajMestimaForma forma) {
        this.forma = forma;
    }

    public void setupButtons() {
        forma.getBtnDodaj().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMesto();
            }
        });
    }

    public void loadMesta() {
        try {
            List<Mesto> mesta = Komunikacija.getInstanca().ucitajMesta();
            MestoTableModel model = new MestoTableModel(mesta);
            forma.getTblMesta().setModel(model);
        } catch (Exception e) {
            UIHelper.showError(forma, "Greska pri ucitavanju mesta", e);
        }
    }

    private void addMesto() {
        String naziv = JOptionPane.showInputDialog(forma, "Unesite naziv mesta:");
        if (naziv == null || naziv.trim().isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Naziv ne sme biti prazan!", "Greska", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String adresa = JOptionPane.showInputDialog(forma, "Unesite adresu mesta (opciono):");
        if (adresa != null && adresa.trim().isEmpty()) {
            adresa = null;
        }

        Mesto mesto = new Mesto();
        mesto.setNaziv(naziv.trim());
        mesto.setAdresa(adresa == null ? null : adresa.trim());

        try {
            Komunikacija.getInstanca().kreirajMesto(mesto);
            loadMesta();
            UIHelper.showInfo(forma, "Sistem je kreirao mesto", "Uspeh");
        } catch (Exception ex) {
            UIHelper.showError(forma, "Sistem ne moze da kreira mesto", ex);
        }
    }
}
