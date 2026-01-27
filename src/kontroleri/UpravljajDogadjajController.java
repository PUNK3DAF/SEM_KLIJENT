/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import domen.Dogadjaj;
import domen.Mesto;
import forme.PrikazDogadjajForma;
import forme.model.DogadjajTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import komunikacija.Komunikacija;

/**
 *
 * @author vldmrk
 */
public class UpravljajDogadjajController {

    private PrikazDogadjajForma forma;

    public UpravljajDogadjajController(PrikazDogadjajForma forma) {
        this.forma = forma;
    }

    public void setupButtons() {
        forma.getBtnDodaj().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDogadjaj();
            }
        });

        forma.getBtnIzmeni().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editDogadjaj();
            }
        });

        forma.getBtnObrisi().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteDogadjaj();
            }
        });
    }

    public void loadDogadjaje() {
        try {
            List<Dogadjaj> dogadjaji = Komunikacija.getInstanca().ucitajDogadjaje();
            DogadjajTableModel model = new DogadjajTableModel(dogadjaji);
            forma.getTblDogadjaji().setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(forma, e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addDogadjaj() {
        String naziv = JOptionPane.showInputDialog(forma, "Unesite naziv događaja:");
        if (naziv == null || naziv.trim().isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Naziv ne sme biti prazan!", "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String datumStr = JOptionPane.showInputDialog(forma, "Unesite datum (yyyy-MM-dd):");
        if (datumStr == null || datumStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Datum je obavezan!", "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate datum;
        try {
            datum = LocalDate.parse(datumStr.trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Nevažeći format datuma!", "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            List<Mesto> mesta = Komunikacija.getInstanca().ucitajMesta();
            if (mesta.isEmpty()) {
                JOptionPane.showMessageDialog(forma, "Nema dostupnih mesta!", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JComboBox<Mesto> comboMesto = new JComboBox<>();
            for (Mesto m : mesta) {
                comboMesto.addItem(m);
            }

            int result = JOptionPane.showConfirmDialog(forma, comboMesto, "Izaberite mesto:",
                    JOptionPane.OK_CANCEL_OPTION);
            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            Mesto izbrano = (Mesto) comboMesto.getSelectedItem();
            if (izbrano == null) {
                JOptionPane.showMessageDialog(forma, "Važeće mesto nije izabrano!", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Dogadjaj dogadjaj = new Dogadjaj();
            dogadjaj.setNaziv(naziv.trim());
            dogadjaj.setDatum(datum);
            dogadjaj.setMestoID(izbrano.getMestoID());
            dogadjaj.setMesto(izbrano);

            Komunikacija.getInstanca().kreirajDogadjaj(dogadjaj);
            loadDogadjaje();
            JOptionPane.showMessageDialog(forma, "Događaj je uspešno dodan!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editDogadjaj() {
        int selectedRow = forma.getTblDogadjaji().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(forma, "Morate selektovati događaj!", "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DogadjajTableModel model = (DogadjajTableModel) forma.getTblDogadjaji().getModel();
        Dogadjaj dogadjaj = model.getDogadjajAt(selectedRow);

        String naziv = JOptionPane.showInputDialog(forma, "Unesite naziv događaja:", dogadjaj.getNaziv());
        if (naziv == null || naziv.trim().isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Naziv ne sme biti prazan!", "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String datumStr = JOptionPane.showInputDialog(forma, "Unesite datum (yyyy-MM-dd):", dogadjaj.getDatum().toString());
        if (datumStr == null || datumStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Datum je obavezan!", "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate datum;
        try {
            datum = LocalDate.parse(datumStr.trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Nevažeći format datuma!", "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            List<Mesto> mesta = Komunikacija.getInstanca().ucitajMesta();
            if (mesta.isEmpty()) {
                JOptionPane.showMessageDialog(forma, "Nema dostupnih mesta!", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JComboBox<Mesto> comboMesto = new JComboBox<>();
            for (Mesto m : mesta) {
                comboMesto.addItem(m);
            }
            comboMesto.setSelectedItem(dogadjaj.getMesto());

            int result = JOptionPane.showConfirmDialog(forma, comboMesto, "Izaberite mesto:",
                    JOptionPane.OK_CANCEL_OPTION);
            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            Mesto izbrano = (Mesto) comboMesto.getSelectedItem();
            if (izbrano == null) {
                JOptionPane.showMessageDialog(forma, "Važeće mesto nije izabrano!", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dogadjaj.setNaziv(naziv.trim());
            dogadjaj.setDatum(datum);
            dogadjaj.setMestoID(izbrano.getMestoID());
            dogadjaj.setMesto(izbrano);

            Komunikacija.getInstanca().izmeniDogadjaj(dogadjaj);
            loadDogadjaje();
            JOptionPane.showMessageDialog(forma, "Događaj je uspešno izmenjen!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDogadjaj() {
        int selectedRow = forma.getTblDogadjaji().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(forma, "Morate selektovati događaj!", "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(forma,
                "Da li ste sigurni da želite da obrišete ovaj događaj?",
                "Potvrda brisanja",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            DogadjajTableModel model = (DogadjajTableModel) forma.getTblDogadjaji().getModel();
            Dogadjaj dogadjaj = model.getDogadjajAt(selectedRow);

            try {
                Komunikacija.getInstanca().obrisiDogadjaj(dogadjaj);
                loadDogadjaje();
                JOptionPane.showMessageDialog(forma, "Događaj je uspešno obrisan!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(forma, ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
