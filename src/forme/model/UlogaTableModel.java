package forme.model;

import domen.Uloga;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class UlogaTableModel extends AbstractTableModel {

    private List<Uloga> uloge;
    private final String[] kolone = {"ID", "Naziv"};

    public UlogaTableModel() {
        this.uloge = new ArrayList<>();
    }

    public void setUloge(List<Uloga> uloge) {
        if (uloge == null) {
            this.uloge = new ArrayList<>();
        } else {
            this.uloge = uloge;
        }
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return uloge.size();
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
        if (uloge == null || rowIndex < 0 || rowIndex >= uloge.size()) {
            return "N/A";
        }
        Uloga u = uloge.get(rowIndex);
        if (u == null) {
            return "N/A";
        }
        switch (columnIndex) {
            case 0:
                return u.getUlogaID();
            case 1:
                return u.getNaziv();
            default:
                return "N/A";
        }
    }

    public Uloga vratiRed(int row) {
        if (row < 0 || row >= uloge.size()) {
            return null;
        }
        return uloge.get(row);
    }
}
