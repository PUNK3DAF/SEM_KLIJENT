package forme.model;

import domen.ClanDrustva;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Model za JTable koji prikazuje članove i njihove uloge
 */
public class ModelTabeleClanovaUloge extends AbstractTableModel {
    private List<ClanSaUlogom> clanovi = new ArrayList<>();
    private String[] kolone = {"Član", "Prezime", "Uloga"};

    public ModelTabeleClanovaUloge(List<ClanSaUlogom> clanovi) {
        if (clanovi != null) {
            this.clanovi = clanovi;
        }
    }

    @Override
    public int getRowCount() {
        return clanovi.size();
    }

    @Override
    public int getColumnCount() {
        return kolone.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ClanSaUlogom cu = clanovi.get(rowIndex);
        if (cu == null || cu.getClan() == null) {
            return null;
        }
        
        switch (columnIndex) {
            case 0:
                return cu.getClan().getImeClan();
            case 1:
                return cu.getClan().getPrezime();
            case 2:
                return cu.getUloga();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return kolone[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Samo uloga kolona je editable
        return columnIndex == 2;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 2 && rowIndex < clanovi.size()) {
            clanovi.get(rowIndex).setUloga(String.valueOf(aValue));
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    public List<ClanSaUlogom> getClanovi() {
        return new ArrayList<>(clanovi);
    }

    public void addClan(ClanSaUlogom clan) {
        clanovi.add(clan);
        fireTableRowsInserted(clanovi.size() - 1, clanovi.size() - 1);
    }

    public void removeClan(int index) {
        if (index >= 0 && index < clanovi.size()) {
            clanovi.remove(index);
            fireTableRowsDeleted(index, index);
        }
    }
}
