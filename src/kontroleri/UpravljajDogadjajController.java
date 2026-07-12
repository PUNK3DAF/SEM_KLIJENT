/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import domen.Ansambl;
import domen.Dogadjaj;
import domen.Mesto;
import forme.PrikazDogadjajForma;
import forme.UIHelper;
import forme.model.DogadjajTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
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
            adjustDogadjajiTableColumns(forma.getTblDogadjaji());
        } catch (Exception e) {
            UIHelper.showError(forma, "Greška pri učitavanju događaja", e);
        }
    }

    private void addDogadjaj() {
        String naziv = JOptionPane.showInputDialog(forma, "Unesite naziv događaja:");
        if (naziv == null || naziv.trim().isEmpty()) {
            UIHelper.showError(forma, "Sistem ne može da kreira događaj\nRazlog: Naziv ne sme biti prazan");
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
                UIHelper.showError(forma, "Sistem ne može da kreira događaj\nRazlog: Nema dostupnih mesta");
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
                UIHelper.showError(forma, "Sistem ne može da kreira događaj\nRazlog: Važeće mesto nije izabrano");
                return;
            }

            Ansambl ansambl = selectAnsambl("Sistem ne može da kreira događaj", null);
            if (ansambl == null) {
                return;
            }

            Dogadjaj dogadjaj = new Dogadjaj();
            dogadjaj.setNaziv(naziv.trim());
            dogadjaj.setDatum(datum);
            dogadjaj.setMesto(izbrano);
            dogadjaj.setAnsambl(ansambl);

            Komunikacija.getInstanca().kreirajDogadjaj(dogadjaj);
            loadDogadjaje();
            UIHelper.showInfo(forma, "Sistem je kreirao događaj", "Uspeh");
        } catch (Exception ex) {
            UIHelper.showError(forma, "Sistem ne može da kreira događaj", ex);
        }
    }

    private void editDogadjaj() {
        int selectedRow = forma.getTblDogadjaji().getSelectedRow();
        if (selectedRow == -1) {
            UIHelper.showError(forma, "Sistem ne može da učita događaj\nRazlog: Nije selektovan događaj");
            return;
        }

        DogadjajTableModel model = (DogadjajTableModel) forma.getTblDogadjaji().getModel();
        Dogadjaj dogadjaj = model.getDogadjajAt(selectedRow);

        UIHelper.showInfo(forma, "Sistem je učitao događaj", "Uspeh");

        String naziv = JOptionPane.showInputDialog(forma, "Unesite naziv događaja:", dogadjaj.getNaziv());
        if (naziv == null || naziv.trim().isEmpty()) {
            UIHelper.showError(forma, "Sistem ne može da zapamti događaj\nRazlog: Naziv ne sme biti prazan");
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
                UIHelper.showError(forma, "Sistem ne može da zapamti događaj\nRazlog: Nema dostupnih mesta");
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
                UIHelper.showError(forma, "Sistem ne može da zapamti događaj\nRazlog: Važeće mesto nije izabrano");
                return;
            }

            Ansambl ansambl = selectAnsambl("Sistem ne može da zapamti događaj", dogadjaj.getAnsambl());
            if (ansambl == null) {
                return;
            }

            dogadjaj.setNaziv(naziv.trim());
            dogadjaj.setDatum(datum);
            dogadjaj.setMesto(izbrano);
            dogadjaj.setAnsambl(ansambl);

            Komunikacija.getInstanca().izmeniDogadjaj(dogadjaj);
            loadDogadjaje();
            UIHelper.showInfo(forma, "Sistem je zapamtio događaj", "Uspeh");
        } catch (Exception ex) {
            UIHelper.showError(forma, "Sistem ne može da zapamti događaj", ex);
        }
    }

    private void deleteDogadjaj() {
        int selectedRow = forma.getTblDogadjaji().getSelectedRow();
        if (selectedRow == -1) {
            UIHelper.showError(forma, "Sistem ne može da obriše događaj\nRazlog: Nije selektovan događaj");
            return;
        }

        DogadjajTableModel model = (DogadjajTableModel) forma.getTblDogadjaji().getModel();
        Dogadjaj dogadjaj = model.getDogadjajAt(selectedRow);

        UIHelper.showInfo(forma, "Sistem je učitao događaj", "Uspeh");

        int confirm = JOptionPane.showConfirmDialog(forma,
                "Da li ste sigurni da želite da obrišete ovaj događaj?",
                "Potvrda brisanja",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            try {
                Komunikacija.getInstanca().obrisiDogadjaj(dogadjaj);
                loadDogadjaje();
                UIHelper.showInfo(forma, "Sistem je obrisao događaj", "Uspeh");
            } catch (Exception ex) {
                UIHelper.showError(forma, "Sistem ne može da obriše događaj", ex);
            }
        }
    }

    private Ansambl selectAnsambl(String errorPrefix, Ansambl preselected) throws Exception {
        List<Ansambl> ansambli = Komunikacija.getInstanca().ucitajAnsamble();
        if (ansambli == null || ansambli.isEmpty()) {
            UIHelper.showError(forma, errorPrefix + "\nRazlog: Ne postoji nijedan ansambl");
            return null;
        }

        JComboBox<Ansambl> comboAnsambl = new JComboBox<>();
        for (Ansambl a : ansambli) {
            comboAnsambl.addItem(a);
        }
        if (preselected != null) {
            comboAnsambl.setSelectedItem(preselected);
        }

        int result = JOptionPane.showConfirmDialog(forma, comboAnsambl, "Izaberite ansambl:",
                JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        Ansambl izabrani = (Ansambl) comboAnsambl.getSelectedItem();
        if (izabrani == null) {
            UIHelper.showError(forma, errorPrefix + "\nRazlog: Važeći ansambl nije izabran");
            return null;
        }

        return izabrani;
    }

    private void adjustDogadjajiTableColumns(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int rowCount = table.getRowCount();

        for (int col = 0; col < table.getColumnCount(); col++) {
            TableColumn column = table.getColumnModel().getColumn(col);
            int width = 50;

            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
            java.awt.Component headerComp = headerRenderer.getTableCellRendererComponent(
                    table, column.getHeaderValue(), false, false, 0, col);
            width = Math.max(width, headerComp.getPreferredSize().width + 12);

            for (int row = 0; row < rowCount; row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, col);
                java.awt.Component cellComp = cellRenderer.getTableCellRendererComponent(
                        table, table.getValueAt(row, col), false, false, row, col);
                width = Math.max(width, cellComp.getPreferredSize().width + 12);
            }

            column.setPreferredWidth(Math.min(width, 400));
        }
    }
}
