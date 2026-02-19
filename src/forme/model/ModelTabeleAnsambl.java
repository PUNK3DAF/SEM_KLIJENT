package forme.model;

import domen.Ansambl;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author vldmrk
 */
public class ModelTabeleAnsambl extends AbstractTableModel {

    List<Ansambl> lista;
    List<Ansambl> originalLista;
    String[] kolone = {"ID", "Ime", "Opis", "Zanr", "Admin ID"};

    public ModelTabeleAnsambl(List<Ansambl> lista) {
        if (lista == null) {
            this.originalLista = new ArrayList<>();
            this.lista = new ArrayList<>();
        } else {
            this.originalLista = new ArrayList<>(lista);
            this.lista = new ArrayList<>(lista);
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
        Ansambl a = lista.get(rowIndex);
        if (a == null) {
            return "N/A";
        }
        return switch (columnIndex) {
            case 0 ->
                a.getAnsamblID();
            case 1 ->
                a.getImeAnsambla();
            case 2 ->
                a.getOpisAnsambla();
            case 3 ->
                (a.getZanr() == null) ? "" : a.getZanr().getNaziv();
            case 4 ->
                (a.getAdmin() == null) ? null : a.getAdmin().getAdminID();
            default ->
                "N/A";
        };
    }

    public List<Ansambl> getLista() {
        return lista;
    }

    public void pretrazi(String ime, String opis, String admin) {
        String imeFilter = (ime == null) ? "" : ime.trim().toLowerCase();
        String opisFilter = (opis == null) ? "" : opis.trim().toLowerCase();
        String adminFilter = (admin == null) ? "" : admin.trim();

        List<Ansambl> filtriranaLista = originalLista.stream()
                .filter(a -> {
                    String aIme = (a.getImeAnsambla() == null) ? "" : a.getImeAnsambla().toLowerCase();
                    return imeFilter.isEmpty() || aIme.contains(imeFilter);
                })
                .filter(a -> {
                    String aOpis = (a.getOpisAnsambla() == null) ? "" : a.getOpisAnsambla().toLowerCase();
                    return opisFilter.isEmpty() || aOpis.contains(opisFilter);
                })
                .filter(a -> {
                    if (adminFilter.isEmpty()) {
                        return true;
                    }
                    try {
                        int adminId = Integer.parseInt(adminFilter);
                        return a.getAdmin() != null && a.getAdmin().getAdminID() == adminId;
                    } catch (NumberFormatException ex) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
        this.lista = filtriranaLista;
        fireTableDataChanged();
    }

}
