/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package forme.model;

import domen.Dogadjaj;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author vldmrk
 */
public class DogadjajTableModel extends AbstractTableModel {

    List<Dogadjaj> dogadjaji;
    String[] columnNames = new String[]{"ID", "Naziv", "Datum", "Mesto"};
    Class[] columnClass = new Class[]{Integer.class, String.class, String.class, String.class};

    public DogadjajTableModel(List<Dogadjaj> dogadjaji) {
        this.dogadjaji = dogadjaji;
    }

    @Override
    public int getRowCount() {
        if (dogadjaji == null) {
            return 0;
        }
        return dogadjaji.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Dogadjaj d = dogadjaji.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return d.getDogadjajID();
            case 1:
                return d.getNaziv();
            case 2:
                return d.getDatum();
            case 3:
                return d.getMesto() != null ? d.getMesto().getNaziv() : "N/A";
            default:
                return "N/A";
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClass[columnIndex];
    }

    public Dogadjaj getDogadjajAt(int row) {
        return dogadjaji.get(row);
    }

    public void setDogadjaji(List<Dogadjaj> dogadjaji) {
        this.dogadjaji = dogadjaji;
        fireTableDataChanged();
    }
}
