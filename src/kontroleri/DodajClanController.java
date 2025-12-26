package kontroleri;

import domen.ClanDrustva;
import forme.DodajClanForma;
import forme.FormaMod;
import forme.UIHelper;
import komunikacija.Konstante;

public class DodajClanController {

    private final DodajClanForma dcf;

    public DodajClanController(DodajClanForma dcf) {
        this.dcf = dcf;
        addActionListeners();
    }

    private void addActionListeners() {
        dcf.addDodajActionListener(e -> handleDodaj());
        dcf.addAzurirajActionListener(e -> handleAzuriraj());
    }

    private void handleDodaj() {
        String ime = dcf.getjTextFieldIme().getText().trim();
        String pol = dcf.getjTextFieldPol().getText().trim();
        String godS = dcf.getjTextFieldGod().getText().trim();
        String tel = dcf.getjTextFieldTel().getText().trim();

        int god = parseGodine(godS);
        if (god == -1) return;

        domen.Administrator admin = coordinator.Coordinator.getInstanca().getAdmin();
        ClanDrustva c = new ClanDrustva(-1, ime, pol, god, tel, admin);

        try {
            komunikacija.Komunikacija.getInstanca().kreirajClanaDrustva(c);
            UIHelper.showInfo(dcf, Konstante.MEMBER_CREATED);
            dcf.dispose();
            coordinator.Coordinator.getInstanca().osveziClanFormu();
        } catch (Exception ex) {
            UIHelper.showError(dcf, Konstante.ERROR_CREATE_MEMBER, ex);
        }
    }

    private void handleAzuriraj() {
        String ime = dcf.getjTextFieldIme().getText().trim();
        String pol = dcf.getjTextFieldPol().getText().trim();
        String godS = dcf.getjTextFieldGod().getText().trim();
        String tel = dcf.getjTextFieldTel().getText().trim();

        int god = parseGodine(godS);
        if (god == -1) return;

        ClanDrustva original = (ClanDrustva) coordinator.Coordinator.getInstanca().vratiParam("clan");
        if (original == null) {
            UIHelper.showError(dcf, Konstante.MEMBER_NOT_AVAILABLE);
            return;
        }

        original.setClanIme(ime);
        original.setClanPol(pol);
        original.setClanGod(god);
        original.setClanBrTel(tel);

        try {
            komunikacija.Komunikacija.getInstanca().izmeniClanaDrustva(original);
            UIHelper.showInfo(dcf, Konstante.MEMBER_SAVED);
            dcf.dispose();
            coordinator.Coordinator.getInstanca().osveziClanFormu();
        } catch (Exception ex) {
            UIHelper.showError(dcf, Konstante.ERROR_SAVE_MEMBER, ex);
            refreshFormFromServer(original);
        }
    }

    private int parseGodine(String godS) {
        if (godS.isEmpty()) return 0;
        try {
            return Integer.parseInt(godS);
        } catch (NumberFormatException ex) {
            UIHelper.showError(dcf, Konstante.ERROR_YEARS_INVALID);
            return -1;
        }
    }

    private void refreshFormFromServer(ClanDrustva original) {
        if (original == null || original.getClanID() <= 0) return;

        new javax.swing.SwingWorker<ClanDrustva, Void>() {
            @Override
            protected ClanDrustva doInBackground() throws Exception {
                return komunikacija.Komunikacija.getInstanca().ucitajClanaDrustva(original.getClanID());
            }

            @Override
            protected void done() {
                try {
                    ClanDrustva fresh = get();
                    if (fresh != null) {
                        coordinator.Coordinator.getInstanca().dodajParam("clan", fresh);
                        dcf.getjTextFieldIme().setText(fresh.getClanIme() == null ? "" : fresh.getClanIme());
                        dcf.getjTextFieldPol().setText(fresh.getClanPol() == null ? "" : fresh.getClanPol());
                        dcf.getjTextFieldGod().setText(fresh.getClanGod() > 0 ? String.valueOf(fresh.getClanGod()) : "");
                        dcf.getjTextFieldTel().setText(fresh.getClanBrTel() == null ? "" : fresh.getClanBrTel());
                    } else {
                        clearForm();
                    }
                } catch (Exception ex) {
                    // Silent fail
                }
            }
        }.execute();
    }

    private void clearForm() {
        dcf.getjTextFieldIme().setText("");
        dcf.getjTextFieldPol().setText("");
        dcf.getjTextFieldGod().setText("");
        dcf.getjTextFieldTel().setText("");
    }

    public void otvoriFormu(FormaMod fm) {
        pripremiFormu(fm);
        dcf.setVisible(true);
    }

    private void pripremiFormu(FormaMod fm) {
        switch (fm) {
            case DODAJ:
                dcf.getjButtonAzuriraj().setVisible(false);
                dcf.getjButtonDodaj().setVisible(true);
                dcf.getjButtonDodaj().setEnabled(true);
                break;
            case IZMENI:
                dcf.getjButtonDodaj().setVisible(false);
                dcf.getjButtonAzuriraj().setVisible(true);
                dcf.getjButtonAzuriraj().setEnabled(true);
                ClanDrustva c = (ClanDrustva) coordinator.Coordinator.getInstanca().vratiParam("clan");
                if (c != null) {
                    dcf.getjTextFieldIme().setText(c.getClanIme());
                    dcf.getjTextFieldPol().setText(c.getClanPol());
                    dcf.getjTextFieldGod().setText(String.valueOf(c.getClanGod()));
                    dcf.getjTextFieldTel().setText(c.getClanBrTel());
                }
                break;
            default:
                throw new AssertionError();
        }
    }
}
