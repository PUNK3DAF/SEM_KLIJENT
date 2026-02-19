package kontroleri;

import domen.Ansambl;
import domen.ClanDrustva;
import domen.Ucesce;
import domen.Zanr;
import forme.DodajAnsamblForma;
import forme.FormaMod;
import forme.UIHelper;
import forme.UpravljajClanovimaForma;
import komunikacija.Konstante;
import java.util.ArrayList;
import java.util.List;

public class DodajAnsamblController {

    private final DodajAnsamblForma daf;

    public DodajAnsamblController(DodajAnsamblForma daf) {
        this.daf = daf;
        addActionListeners();
    }

    private void addActionListeners() {
        daf.DodajAddActionListener(e -> handleDodaj());
        daf.AzurirajAddActionListener(e -> handleAzuriraj());
        daf.UpravljajClanovimaAddActionListener(e -> openMemberManager());
    }

    private void handleDodaj() {
        String ime = daf.getjTextFieldImeAns().getText().trim();
        String opis = daf.getjTextAreaOpisAns().getText().trim();

        if (!validateInput(ime, opis, Konstante.ERROR_CREATE_ENSEMBLE)) {
            return;
        }

        Zanr zanr = selektujZanr();
        if (zanr == null) {
            return;
        }

        domen.Administrator admin = coordinator.Coordinator.getInstanca().getAdmin();
        Ansambl a = new Ansambl(-1, ime, opis, admin);
        a.setZanr(zanr);

        List<Ucesce> ucesca = getUcescaFromCoordinator();
        if (ucesca != null) {
            a.setUcesca(ucesca);
        }

        try {
            komunikacija.Komunikacija.getInstanca().kreirajAnsambl(a);
            UIHelper.showInfo(daf, Konstante.ENSEMBLE_CREATED);
            daf.dispose();
            coordinator.Coordinator.getInstanca().osveziGlavnuFormu();
        } catch (Exception ex) {
            UIHelper.showError(daf, Konstante.ERROR_CREATE_ENSEMBLE, ex);
        }
    }

    private void handleAzuriraj() {
        String ime = daf.getjTextFieldImeAns().getText().trim();
        String opis = daf.getjTextAreaOpisAns().getText().trim();

        if (!validateInput(ime, opis, Konstante.ERROR_SAVE_ENSEMBLE)) {
            return;
        }

        Ansambl original = (Ansambl) coordinator.Coordinator.getInstanca().vratiParam("Ansambl");
        if (original == null) {
            UIHelper.showError(daf, Konstante.ERROR_LOAD_ENSEMBLE);
            return;
        }

        Zanr zanr = selektujZanr(original.getZanr());
        if (zanr == null) {
            return;
        }

        original.setImeAnsambla(ime);
        original.setOpisAnsambla(opis);
        original.setZanr(zanr);

        List<Ucesce> ucesca = getUcescaFromCoordinator();
        original.setUcesca(ucesca);

        try {
            komunikacija.Komunikacija.getInstanca().izmenaAnsambla(original);
            UIHelper.showInfo(daf, Konstante.ENSEMBLE_SAVED);
            daf.dispose();
            coordinator.Coordinator.getInstanca().osveziFormu();
            coordinator.Coordinator.getInstanca().osveziGlavnuFormu();
        } catch (Exception ex) {
            UIHelper.showError(daf, Konstante.ERROR_SAVE_ENSEMBLE, ex);
            refreshFormFromServer(original);
        }
    }

    private boolean validateInput(String ime, String opis, String errorMessage) {
        if (!ime.isEmpty() && !opis.isEmpty()) {
            return true;
        }

        String reason = ime.isEmpty() && opis.isEmpty()
                ? Konstante.ERROR_REQUIRED_FIELDS
                : ime.isEmpty()
                ? Konstante.ERROR_REQUIRED_NAME
                : Konstante.ERROR_REQUIRED_DESCRIPTION;

        UIHelper.showError(daf, errorMessage + "\nRazlog: " + reason);
        return false;
    }

    private List<Ucesce> getUcescaFromCoordinator() {
        Object obj = coordinator.Coordinator.getInstanca().vratiParam("izabraniClanovi");
        if (obj == null || !(obj instanceof java.util.List)) {
            return null;
        }

        java.util.List<?> list = (java.util.List<?>) obj;
        java.util.Map<Integer, Integer> uloge = new java.util.HashMap<>();
        Object rolesObj = coordinator.Coordinator.getInstanca().vratiParam("ulogeClanova");
        if (rolesObj instanceof java.util.Map) {
            try {
                uloge = (java.util.Map<Integer, Integer>) rolesObj;
            } catch (Exception ignored) {
            }
        }

        List<Ucesce> res = new ArrayList<>();
        for (Object o : list) {
            if (o instanceof ClanDrustva) {
                ClanDrustva c = (ClanDrustva) o;
                Ucesce u = new Ucesce();
                u.setClan(c);
                Integer roleId = uloge.getOrDefault(c.getClanID(), 1);
                u.setUloga(String.valueOf(roleId));
                res.add(u);
            }
        }

        return res.isEmpty() ? null : res;
    }

    private void refreshFormFromServer(Ansambl original) {
        if (original == null || original.getAnsamblID() <= 0) {
            return;
        }

        new javax.swing.SwingWorker<Ansambl, Void>() {
            @Override
            protected Ansambl doInBackground() throws Exception {
                return komunikacija.Komunikacija.getInstanca().ucitajAnsambl(original.getAnsamblID());
            }

            @Override
            protected void done() {
                try {
                    Ansambl fresh = get();
                    if (fresh != null) {
                        coordinator.Coordinator.getInstanca().dodajParam("Ansambl", fresh);
                        daf.getjTextFieldImeAns().setText(fresh.getImeAnsambla() == null ? "" : fresh.getImeAnsambla());
                        daf.getjTextAreaOpisAns().setText(fresh.getOpisAnsambla() == null ? "" : fresh.getOpisAnsambla());
                    } else {
                        daf.getjTextFieldImeAns().setText("");
                        daf.getjTextAreaOpisAns().setText("");
                    }
                } catch (Exception ex) {
                }
            }
        }.execute();
    }

    private void openMemberManager() {
        UpravljajClanovimaForma dlg = new UpravljajClanovimaForma(daf, true);
        new UpravljajClanovimaController(dlg).open();
    }

    private Zanr selektujZanr() {
        return selektujZanr(null);
    }

    private Zanr selektujZanr(Zanr defaultZanr) {
        List<Zanr> zanrovi = new ArrayList<>();
        try {
            zanrovi = komunikacija.Komunikacija.getInstanca().ucitajZanrove();
        } catch (Exception ex) {
            UIHelper.showError(daf, "Greska pri ucitavanju zanrova", ex);
            return null;
        }

        if (zanrovi == null || zanrovi.isEmpty()) {
            UIHelper.showError(daf, "Nema dostupnih zanrova. Prvo kreirajte zanr.");
            return null;
        }

        Zanr selected = (defaultZanr != null) ? defaultZanr : zanrovi.get(0);
        javax.swing.JComboBox<Zanr> combo = new javax.swing.JComboBox<>(zanrovi.toArray(new Zanr[0]));
        combo.setSelectedItem(selected);

        int res = javax.swing.JOptionPane.showConfirmDialog(
                daf,
                combo,
                "Odaberite zanr za ansambl",
                javax.swing.JOptionPane.OK_CANCEL_OPTION,
                javax.swing.JOptionPane.PLAIN_MESSAGE
        );

        if (res == javax.swing.JOptionPane.OK_OPTION && combo.getSelectedItem() instanceof Zanr) {
            return (Zanr) combo.getSelectedItem();
        }
        return null;
    }

    public void otvoriFormu(FormaMod fm) {
        pripremiFormu(fm);
        daf.setVisible(true);
    }

    private void pripremiFormu(FormaMod fm) {
        switch (fm) {
            case DODAJ:
                daf.getjButtonAzuriraj().setVisible(false);
                daf.getjButtonDodaj().setVisible(true);
                daf.getjButtonDodaj().setEnabled(true);
                break;
            case IZMENI:
                daf.getjButtonDodaj().setVisible(false);
                daf.getjButtonAzuriraj().setVisible(true);
                daf.getjButtonAzuriraj().setEnabled(true);
                Ansambl a = (Ansambl) coordinator.Coordinator.getInstanca().vratiParam("Ansambl");
                daf.getjTextFieldImeAns().setText(a.getImeAnsambla());
                daf.getjTextAreaOpisAns().setText(a.getOpisAnsambla());
                break;
            default:
                throw new AssertionError();
        }
    }
}
