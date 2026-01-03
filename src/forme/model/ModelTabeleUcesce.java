package forme.model;

import domen.Ucesce;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author vldmrk
 */
public class ModelTabeleUcesce extends AbstractTableModel {

    private List<Ucesce> lista;
    private String[] kolone = {"Ansambl", "Clan", "Uloga"};

    public ModelTabeleUcesce(List<Ucesce> lista) {
        if (lista == null) {
            this.lista = new ArrayList<>();
        } else {
            this.lista = lista;
        }
    }

    @Override
    public int getRowCount() {
        return lista.size();
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
        if (lista == null || rowIndex < 0 || rowIndex >= lista.size()) {
            return "N/A";
        }
        Ucesce u = lista.get(rowIndex);
        if (u == null) {
            return "N/A";
        }
        switch (columnIndex) {
            case 0:
                if (u.getAnsambl() == null) return null;
                String ansIme = u.getAnsambl().getImeAnsambla();
                return ansIme != null ? ansIme : u.getAnsambl().getAnsamblID();
            case 1:
                if (u.getClan() == null) return null;
                String clanIme = u.getClan().getClanIme();
                return clanIme != null ? clanIme : u.getClan().getClanID();
            case 2:
                return u.getUloga();
            default:
                return "N/A";
        }
    }

    public List<Ucesce> getLista() {
        return lista;
    }

    public void setLista(List<Ucesce> nova) {
        if (nova == null) {
            nova = new ArrayList<>();
        }
        this.lista = nova;
        fireTableDataChanged();
    }
}
