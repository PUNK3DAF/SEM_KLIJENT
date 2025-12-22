package forme.model;

import domen.ClanDrustva;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author vldmrk
 */
public class ModelTabeleClan extends AbstractTableModel {

    private List<ClanDrustva> originalLista;
    private List<ClanDrustva> lista;
    String[] kolone = {"ID", "Ime", "Pol", "Godine", "Telefon", "Admin ID"};

    public ModelTabeleClan(List<ClanDrustva> lista) {
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
        ClanDrustva c = lista.get(rowIndex);
        if (c == null) {
            return "N/A";
        }
        switch (columnIndex) {
            case 0:
                return c.getClanID();
            case 1:
                return c.getClanIme();
            case 2:
                return c.getClanPol();
            case 3:
                return c.getClanGod();
            case 4:
                return c.getClanBrTel();
            case 5:
                return (c.getAdmin() == null) ? null : c.getAdmin().getAdminID();
            default:
                return "N/A";
        }
    }

    public List<ClanDrustva> getLista() {
        return lista;
    }

    public void pretrazi(String ime, String pol, String god, String admin) {
        String imeFilter = (ime == null) ? "" : ime.trim().toLowerCase();
        String polFilter = (pol == null) ? "" : pol.trim().toLowerCase();
        String godFilter = (god == null) ? "" : god.trim();
        String adminFilter = (admin == null) ? "" : admin.trim();

        List<ClanDrustva> filtriranaLista = originalLista.stream()
                .filter(c -> {
                    String cIme = (c.getClanIme() == null) ? "" : c.getClanIme().toLowerCase();
                    return imeFilter.isEmpty() || cIme.contains(imeFilter);
                })
                .filter(c -> {
                    String cPol = (c.getClanPol() == null) ? "" : c.getClanPol().toLowerCase();
                    return polFilter.isEmpty() || cPol.contains(polFilter);
                })
                .filter(c -> {
                    if (godFilter.isEmpty()) {
                        return true;
                    }
                    try {
                        int g = Integer.parseInt(godFilter);
                        return c.getClanGod() == g;
                    } catch (NumberFormatException ex) {
                        return false;
                    }
                }).filter(c -> {
            if (adminFilter.isEmpty()) {
                return true;
            }
            try {
                int aId = Integer.parseInt(adminFilter);
                return c.getAdmin() != null && c.getAdmin().getAdminID() == aId;
            } catch (NumberFormatException ex) {
                return false;
            }
        })
                .collect(Collectors.toList());

        this.lista = filtriranaLista;
        fireTableDataChanged();
    }

}
