package kontroleri;

import domen.Ansambl;
import forme.PrikazAnsambalaForma;
import forme.UIHelper;
import forme.model.ModelTabeleAnsambl;
import komunikacija.Konstante;
import java.util.List;

public class PrikazAnsambalaController {

    private final PrikazAnsambalaForma paf;

    public PrikazAnsambalaController(PrikazAnsambalaForma paf) {
        this.paf = paf;
        addActionListeners();
    }

    private void addActionListeners() {
        paf.addBtnObrisiActionListener(e -> handleObrisiAnsambl());
        paf.addBtnAzurirajActionListener(e -> handleAzurirajAnsambl());
        paf.addBtnPretragaActionListener(e -> handlePretraga());
        paf.addBtnPrikaziActionListener(e -> handlePrikaziAnsambl());
    }

    private void handleObrisiAnsambl() {
        Ansambl sel = getSelectedAnsambl();
        if (sel == null) return;

        ucitajAnsamblUPozadini(sel.getAnsamblID(), full -> {
            UIHelper.showInfo(paf, Konstante.ENSEMBLE_LOADED);
            if (UIHelper.confirm(paf, Konstante.CONFIRM_DELETE) != 0) return;
            brisiAnsamblUPozadini(full);
        });
    }

    private void handleAzurirajAnsambl() {
        Ansambl sel = getSelectedAnsambl();
        if (sel == null) return;

        ucitajAnsamblUPozadini(sel.getAnsamblID(), full -> {
            UIHelper.showInfo(paf, Konstante.ENSEMBLE_LOADED);
            coordinator.Coordinator.getInstanca().dodajParam("Ansambl", full);
            coordinator.Coordinator.getInstanca().otvoriIzmeniAnsamblFormu();
        });
    }

    private void handlePretraga() {
        String ime = paf.getjTextFieldIme().getText().trim();
        String opis = paf.getjTextFieldOpis().getText().trim();
        String admin = paf.getjTextFieldAdmin().getText().trim();
        ModelTabeleAnsambl mta = (ModelTabeleAnsambl) paf.getjTableAnsambli().getModel();
        mta.pretrazi(ime, opis, admin);

        String poruka = mta.getLista() == null || mta.getLista().isEmpty()
                ? Konstante.SEARCH_NOT_FOUND
                : Konstante.SEARCH_FOUND;
        UIHelper.showInfo(paf, poruka);
    }

    private void handlePrikaziAnsambl() {
        Ansambl sel = getSelectedAnsambl();
        if (sel == null) return;

        ucitajAnsamblUPozadini(sel.getAnsamblID(), full -> {
            String detalji = formatAnsambl(full);
            UIHelper.showInfo(paf, detalji + "\n\n" + Konstante.ENSEMBLE_LOADED, Konstante.TITLE_ENSEMBLE_DETAILS);
        });
    }

    private Ansambl getSelectedAnsambl() {
        int red = paf.getjTableAnsambli().getSelectedRow();
        if (red == -1) {
            UIHelper.showError(paf, Konstante.ENSEMBLE_NOT_SELECTED);
            return null;
        }
        ModelTabeleAnsambl mta = (ModelTabeleAnsambl) paf.getjTableAnsambli().getModel();
        return mta.getLista().get(red);
    }

    private void ucitajAnsamblUPozadini(int id, java.util.function.Consumer<Ansambl> onSuccess) {
        new javax.swing.SwingWorker<Ansambl, Void>() {
            @Override
            protected Ansambl doInBackground() throws Exception {
                return komunikacija.Komunikacija.getInstanca().ucitajAnsambl(id);
            }

            @Override
            protected void done() {
                try {
                    onSuccess.accept(get());
                } catch (Exception ex) {
                    UIHelper.showError(paf, Konstante.ERROR_LOAD_ENSEMBLE, ex);
                }
            }
        }.execute();
    }

    private void brisiAnsamblUPozadini(Ansambl ansambl) {
        new javax.swing.SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                komunikacija.Komunikacija.getInstanca().obrisiAnsambl(ansambl);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    UIHelper.showInfo(paf, Konstante.ENSEMBLE_DELETED);
                    osveziFormu();
                    coordinator.Coordinator.getInstanca().osveziGlavnuFormu();
                } catch (Exception ex) {
                    UIHelper.showError(paf, Konstante.ERROR_DELETE_ENSEMBLE, ex);
                }
            }
        }.execute();
    }

    private String formatAnsambl(Ansambl a) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(a.getAnsamblID()).append("\n");
        sb.append("Ime: ").append(a.getImeAnsambla() == null ? "" : a.getImeAnsambla()).append("\n");
        sb.append("Opis: ").append(a.getOpisAnsambla() == null ? "" : a.getOpisAnsambla()).append("\n");
        sb.append("Zanr: ").append(a.getZanr() == null ? "" : a.getZanr().getNaziv()).append("\n");
        if (a.getAdmin() != null) {
            String ime = a.getAdmin().getAdminIme();
            String user = a.getAdmin().getAdminUsername();
            sb.append("Administrator: ");
            if (ime != null && !ime.isEmpty()) {
                sb.append(ime);
                if (user != null && !user.isEmpty()) {
                    sb.append(" (").append(user).append(")");
                }
            } else if (user != null && !user.isEmpty()) {
                sb.append(user);
            } else {
                sb.append("ID=").append(a.getAdmin().getAdminID());
            }
            sb.append("\n");
        }
        return sb.toString();
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