/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
                return (u.getAnsambl() == null) ? null : u.getAnsambl().getImeAnsambla();
            case 1:
                return (u.getClan() == null) ? null : u.getClan().getClanIme();
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
