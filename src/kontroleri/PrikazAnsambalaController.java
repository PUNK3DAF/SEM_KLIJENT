/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import domen.Ansambl;
import forme.PrikazAnsambalaForma;
import forme.model.ModelTabeleAnsambl;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author vldmrk
 */
public class PrikazAnsambalaController {

    private final PrikazAnsambalaForma paf;

    public PrikazAnsambalaController(PrikazAnsambalaForma paf) {
        this.paf = paf;
        addActionListeners();
    }

    private void addActionListeners() {
        paf.addBtnObrisiActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int red = paf.getjTableAnsambli().getSelectedRow();
                if (red == -1) {
                    JOptionPane.showMessageDialog(paf, "SISTEM NE MOZE DA AZURIRA ANSAMBL", "GRESKA", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                final ModelTabeleAnsambl mta = (ModelTabeleAnsambl) paf.getjTableAnsambli().getModel();
                final Ansambl sel = mta.getLista().get(red);

                new javax.swing.SwingWorker<Ansambl, Void>() {
                    @Override
                    protected Ansambl doInBackground() throws Exception {
                        return komunikacija.Komunikacija.getInstanca().ucitajAnsambl(sel.getAnsamblID());
                    }

                    @Override
                    protected void done() {
                        try {
                            Ansambl full = get(); // moze baciti InterruptedException/ExecutionException
                            JOptionPane.showMessageDialog(paf, "Sistem je ucitao ansambl.", "Informacija", JOptionPane.INFORMATION_MESSAGE);
                            coordinator.Coordinator.getInstanca().dodajParam("Ansambl", full);
                            coordinator.Coordinator.getInstanca().otvoriIzmeniAnsamblFormu();
                        } catch (InterruptedException | java.util.concurrent.ExecutionException ex) {
                            Throwable cause = ex instanceof java.util.concurrent.ExecutionException ? ex.getCause() : ex;
                            String razlog = cause == null || cause.getMessage() == null ? "" : cause.getMessage();
                            String poruka = "Sistem ne moze da ucita ansambl";
                            if (!razlog.isEmpty()) {
                                poruka += "\nRazlog: " + razlog;
                            }
                            JOptionPane.showMessageDialog(paf, poruka, "Greska", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }.execute();
            }
        });
        paf.addBtnAzurirajActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int red = paf.getjTableAnsambli().getSelectedRow();
                if (red == -1) {
                    JOptionPane.showMessageDialog(paf, "SISTEM NE MOZE DA AZURIRA ANSAMBL", "GRESKA", JOptionPane.ERROR_MESSAGE);
                } else {
                    final ModelTabeleAnsambl mta = (ModelTabeleAnsambl) paf.getjTableAnsambli().getModel();
                    final Ansambl sel = mta.getLista().get(red);

                    new javax.swing.SwingWorker<Ansambl, Void>() {
                        @Override
                        protected Ansambl doInBackground() throws Exception {
                            // ucitaj kompletan ansambl sa servera
                            return komunikacija.Komunikacija.getInstanca().ucitajAnsambl(sel.getAnsamblID());
                        }

                        @Override
                        protected void done() {
                            try {
                                Ansambl full = get();
                                // 8) uspesno ucitano
                                JOptionPane.showMessageDialog(paf, "Sistem je ucitao ansambl.", "Informacija", JOptionPane.INFORMATION_MESSAGE);
                                coordinator.Coordinator.getInstanca().dodajParam("Ansambl", full);
                                coordinator.Coordinator.getInstanca().otvoriIzmeniAnsamblFormu();
                            } catch (Exception ex) {
                                // alternativni scenario 8.1
                                String razlog = ex.getMessage() == null ? "" : ex.getMessage();
                                String poruka = "Sistem ne moze da ucita ansambl";
                                if (!razlog.isEmpty()) {
                                    poruka += "\nRazlog: " + razlog;
                                }
                                JOptionPane.showMessageDialog(paf, poruka, "Greska", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }.execute();
                }
            }
        });
        paf.addBtnPretragaActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ime = paf.getjTextFieldIme().getText().trim();
                String opis = paf.getjTextFieldOpis().getText().trim();
                String admin = paf.getjTextFieldAdmin().getText().trim();
                ModelTabeleAnsambl mta = (ModelTabeleAnsambl) paf.getjTableAnsambli().getModel();
                mta.pretrazi(ime, opis, admin);
                if (mta.getLista() == null || mta.getLista().isEmpty()) {
                    JOptionPane.showMessageDialog(paf,
                            "Sistem ne moze da nadje ansamble po zadatoj vrednosti.",
                            "Informacija",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(paf,
                            "Sistem je nasao ansamble po zadatoj vrednosti.",
                            "Informacija",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    public void otvoriFormu() {
        pripremiFormu();
        paf.setVisible(true);
    }

    public void pripremiFormu() {
        List<Ansambl> ansambli = komunikacija.Komunikacija.getInstanca().ucitajAnsamble();
        if (ansambli == null) {
            ansambli = new java.util.ArrayList<>();
        }
        ModelTabeleAnsambl mta = new ModelTabeleAnsambl(ansambli);
        paf.getjTableAnsambli().setModel(mta);
        paf.srediSirinuKolona(paf.getjTableAnsambli());
    }

    public void osveziFormu() {
        pripremiFormu();
    }

}
