/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import domen.Mesto;
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

        forma.getBtnIzmeni().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editMesto();
            }
        });

        forma.getBtnObrisi().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMesto();
            }
        });
    }

    public void loadMesta() {
        try {
            List<Mesto> mesta = Komunikacija.getInstanca().ucitajMesta();
            MestoTableModel model = new MestoTableModel(mesta);
            forma.getTblMesta().setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(forma, e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addMesto() {
        String naziv = JOptionPane.showInputDialog(forma, "Unesite naziv mesta:");
        if (naziv == null || naziv.trim().isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Naziv ne sme biti prazan!", "Greška", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(forma, "Mesto je uspešno dodato!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editMesto() {
        int selectedRow = forma.getTblMesta().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(forma, "Morate selektovati mesto!", "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }

        MestoTableModel model = (MestoTableModel) forma.getTblMesta().getModel();
        Mesto mesto = model.getMestoAt(selectedRow);

        String naziv = JOptionPane.showInputDialog(forma, "Unesite naziv mesta:", mesto.getNaziv());
        if (naziv == null || naziv.trim().isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Naziv ne sme biti prazan!", "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String adresa = JOptionPane.showInputDialog(forma, "Unesite adresu mesta (opciono):", mesto.getAdresa());
        if (adresa != null && adresa.trim().isEmpty()) {
            adresa = null;
        }

        mesto.setNaziv(naziv.trim());
        mesto.setAdresa(adresa == null ? null : adresa.trim());

        try {
            Komunikacija.getInstanca().izmeniMesto(mesto);
            loadMesta();
            JOptionPane.showMessageDialog(forma, "Mesto je uspešno izmenjeno!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMesto() {
        int selectedRow = forma.getTblMesta().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(forma, "Morate selektovati mesto!", "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(forma,
                "Da li ste sigurni da želite da obrišete ovo mesto?",
                "Potvrda brisanja",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            MestoTableModel model = (MestoTableModel) forma.getTblMesta().getModel();
            Mesto mesto = model.getMestoAt(selectedRow);

            try {
                Komunikacija.getInstanca().obrisiMesto(mesto);
                loadMesta();
                JOptionPane.showMessageDialog(forma, "Mesto je uspešno obrisano!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(forma, ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
