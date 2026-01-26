package forme.model;

import domen.Zanr;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ZanrTableModel extends AbstractTableModel {

    private List<Zanr> zanrovi;
    private final String[] kolone = {"ID", "Naziv"};

    public ZanrTableModel() {
        this.zanrovi = new ArrayList<>();
    }

    public void setZanrovi(List<Zanr> zanrovi) {
        if (zanrovi == null) {
            this.zanrovi = new ArrayList<>();
        } else {
            this.zanrovi = zanrovi;
        }
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return zanrovi.size();
    }

    @Override
    public int getColumnCount() {
        return kolone.length;
    }

    @Override
    public String getColumnName(int column) {
        return kolone[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (zanrovi == null || rowIndex < 0 || rowIndex >= zanrovi.size()) {
            return "N/A";
        }
        Zanr z = zanrovi.get(rowIndex);
        if (z == null) {
            return "N/A";
        }
        switch (columnIndex) {
            case 0:
                return z.getZanrID();
            case 1:
                return z.getNaziv();
            default:
                return "N/A";
        }
    }

    public Zanr vratiRed(int row) {
        if (row < 0 || row >= zanrovi.size()) {
            return null;
        }
        return zanrovi.get(row);
    }
}
