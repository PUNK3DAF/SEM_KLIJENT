/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coordinator;

import forme.LoginForma;
import kontroleri.LoginController;

/**
 *
 * @author vldmrk
 */
public class Coordinator {

    private static Coordinator instanca;
    private LoginController loginCont;

    private Coordinator() {
    }

    public static Coordinator getInstanca() {
        if (instanca == null) {
            instanca = new Coordinator();
        }
        return instanca;
    }

    public void otvoriLoginFormu() {
        loginCont = new LoginController(new LoginForma());
        loginCont.otvoriFormu();
    }

}
