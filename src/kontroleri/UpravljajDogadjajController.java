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
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
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
            JOptionPane.showMessageDialog(forma, "Greska pri ucitavanju dogadjaja", "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addDogadjaj() {
        String naziv = JOptionPane.showInputDialog(forma, "Unesite naziv dogadjaja:");
        if (naziv == null || naziv.trim().isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Naziv ne sme biti prazan!", "Greska", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create date picker
        JPanel datumPanel = new JPanel();
        datumPanel.add(new JLabel("Izaberite datum:"));
        
        Calendar cal = Calendar.getInstance();
        SpinnerDateModel dateModel = new SpinnerDateModel(cal.getTime(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner datumSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(datumSpinner, "yyyy-MM-dd");
        datumSpinner.setEditor(editor);
        datumPanel.add(datumSpinner);
        
        int datumResult = JOptionPane.showConfirmDialog(forma, datumPanel, "Izaberite datum",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (datumResult != JOptionPane.OK_OPTION) {
            return;
        }

        java.util.Date selectedDate = (java.util.Date) datumSpinner.getValue();
        java.time.LocalDate datum = selectedDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        try {
            List<Mesto> mesta = Komunikacija.getInstanca().ucitajMesta();
            if (mesta.isEmpty()) {
                JOptionPane.showMessageDialog(forma, "Nema dostupnih mesta!", "Greska", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(forma, "Vazece mesto nije izabrano!", "Greska", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Dogadjaj dogadjaj = new Dogadjaj();
            dogadjaj.setNaziv(naziv.trim());
            dogadjaj.setDatum(datum);
            dogadjaj.setMestoID(izbrano.getMestoID());
            dogadjaj.setMesto(izbrano);

            Komunikacija.getInstanca().kreirajDogadjaj(dogadjaj);
            loadDogadjaje();
            JOptionPane.showMessageDialog(forma, "Sistem je kreirao dogadjaj", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Sistem ne moze da kreira dogadjaj", "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editDogadjaj() {
        int selectedRow = forma.getTblDogadjaji().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(forma, "Morate selektovati dogadjaj!", "Greska", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DogadjajTableModel model = (DogadjajTableModel) forma.getTblDogadjaji().getModel();
        Dogadjaj dogadjaj = model.getDogadjajAt(selectedRow);

        String naziv = JOptionPane.showInputDialog(forma, "Unesite naziv dogadjaja:", dogadjaj.getNaziv());
        if (naziv == null || naziv.trim().isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Naziv ne sme biti prazan!", "Greska", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel datumPanel = new JPanel();
        datumPanel.add(new JLabel("Izaberite datum:"));
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(java.sql.Date.valueOf(dogadjaj.getDatum()));
        SpinnerDateModel dateModel = new SpinnerDateModel(cal.getTime(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner datumSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(datumSpinner, "yyyy-MM-dd");
        datumSpinner.setEditor(editor);
        datumPanel.add(datumSpinner);
        
        int datumResult = JOptionPane.showConfirmDialog(forma, datumPanel, "Izaberite datum",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (datumResult != JOptionPane.OK_OPTION) {
            return;
        }

        java.util.Date selectedDate = (java.util.Date) datumSpinner.getValue();
        java.time.LocalDate datum = selectedDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        try {
            List<Mesto> mesta = Komunikacija.getInstanca().ucitajMesta();
            if (mesta.isEmpty()) {
                JOptionPane.showMessageDialog(forma, "Nema dostupnih mesta!", "Greska", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(forma, "Vazece mesto nije izabrano!", "Greska", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dogadjaj.setNaziv(naziv.trim());
            dogadjaj.setDatum(datum);
            dogadjaj.setMestoID(izbrano.getMestoID());
            dogadjaj.setMesto(izbrano);

            Komunikacija.getInstanca().izmeniDogadjaj(dogadjaj);
            loadDogadjaje();
            JOptionPane.showMessageDialog(forma, "Sistem je zapamtio dogadjaj", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Sistem ne moze da zapamti dogadjaj", "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDogadjaj() {
        int selectedRow = forma.getTblDogadjaji().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(forma, "Morate selektovati dogadjaj!", "Greska", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(forma,
                "Da li ste sigurni da zelite da obrisete ovaj dogadjaj?",
                "Potvrda brisanja",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            DogadjajTableModel model = (DogadjajTableModel) forma.getTblDogadjaji().getModel();
            Dogadjaj dogadjaj = model.getDogadjajAt(selectedRow);

            try {
                Komunikacija.getInstanca().obrisiDogadjaj(dogadjaj);
                loadDogadjaje();
                JOptionPane.showMessageDialog(forma, "Sistem je obrisao dogadjaj", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(forma, "Sistem ne moze da obrisa dogadjaj", "Greska", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
