/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import domen.Administrator;
import forme.LoginForma;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author vldmrk
 */
public class LoginController {

    private final LoginForma lf;

    public LoginController(LoginForma lf) {
        this.lf = lf;
        addActionListeners();
    }

    private void addActionListeners() {
        lf.loginAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prijava(e);
            }

            private void prijava(ActionEvent e) {
                String user = lf.getjTextFieldUsername().getText().trim();
                String pass = String.valueOf(lf.getjPasswordField1().getPassword());

                komunikacija.Komunikacija.getInstanca().konekcija();
                Administrator a = komunikacija.Komunikacija.getInstanca().login(user, pass);

                if (a == null) {
                    JOptionPane.showMessageDialog(lf, "Pogresni kredencijali za prijavu", "GRESKA", JOptionPane.ERROR_MESSAGE);
                } else {
                    coordinator.Coordinator.getInstanca().setAdmin(a);
                    JOptionPane.showMessageDialog(lf, "Uspesno ste se prijavili", "USPEH", JOptionPane.INFORMATION_MESSAGE);
                    coordinator.Coordinator.getInstanca().otvoriGlavnuFormu();
                    lf.dispose();
                }
            }
        });
    }

    public void otvoriFormu() {
        lf.setVisible(true);
    }

}
