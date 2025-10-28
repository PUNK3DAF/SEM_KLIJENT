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
                    JOptionPane.showMessageDialog(lf, "NEUSPESNA PRIJAVA", "GRESKA", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(lf, "USPESNA PRIJAVA", "USPEH", JOptionPane.INFORMATION_MESSAGE);
                    lf.dispose();
                }
            }
        });
    }

    public void otvoriFormu() {
        lf.setVisible(true);
    }

}
