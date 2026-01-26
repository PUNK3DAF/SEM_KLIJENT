/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package forme.model;

import domen.Mesto;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author vldmrk
 */
public class MestoTableModel extends AbstractTableModel {

    List<Mesto> mesta;
    String[] columnNames = new String[]{"ID", "Naziv", "Adresa"};
    Class[] columnClass = new Class[]{Integer.class, String.class, String.class};

    public MestoTableModel(List<Mesto> mesta) {
        this.mesta = mesta;
    }

    @Override
    public int getRowCount() {
        if (mesta == null) {
            return 0;
        }
        return mesta.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Mesto m = mesta.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return m.getMestoID();
            case 1:
                return m.getNaziv();
            case 2:
                return m.getAdresa();
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

    public Mesto getMestoAt(int row) {
        return mesta.get(row);
    }

    public void setMesta(List<Mesto> mesta) {
        this.mesta = mesta;
        fireTableDataChanged();
    }
}
