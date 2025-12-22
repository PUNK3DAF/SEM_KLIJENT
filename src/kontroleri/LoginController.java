package kontroleri;

import domen.Administrator;
import forme.LoginForma;
import forme.UIHelper;

public class LoginController {

    private final LoginForma lf;

    public LoginController(LoginForma lf) {
        this.lf = lf;
        addActionListeners();
    }

    private void addActionListeners() {
        lf.loginAddActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String user = lf.getjTextFieldUsername().getText().trim();
        String pass = String.valueOf(lf.getjPasswordField1().getPassword());

        komunikacija.Komunikacija.getInstanca().konekcija();
        Administrator a = komunikacija.Komunikacija.getInstanca().login(user, pass);

        if (a == null) {
            UIHelper.showError(lf, "Pogresni kredencijali za prijavu");
        } else {
            coordinator.Coordinator.getInstanca().setAdmin(a);
            UIHelper.showInfo(lf, "Uspesno ste se prijavili", "USPEH");
            coordinator.Coordinator.getInstanca().otvoriGlavnuFormu();
            lf.dispose();
        }
    }

    public void otvoriFormu() {
        lf.setVisible(true);
    }
}
