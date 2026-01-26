/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coordinator;

import domen.Administrator;
import forme.DodajAnsamblForma;
import forme.DodajClanForma;
import forme.FormaMod;
import forme.GlavnaForma;
import forme.LoginForma;
import forme.PrikazAnsambalaForma;
import forme.PrikazClanovaForma;
import forme.UpravljajZanrovimaForma;
import java.util.HashMap;
import java.util.Map;
import kontroleri.DodajAnsamblController;
import kontroleri.DodajClanController;
import kontroleri.GlavnaFormaController;
import kontroleri.LoginController;
import kontroleri.PrikazAnsambalaController;
import kontroleri.PrikazClanovaController;
import kontroleri.UpravljajZanrovimaController;

/**
 *
 * @author vldmrk
 */
public class Coordinator {

    private static Coordinator instanca;
    private Administrator admin;
    private LoginController loginCont;
    private GlavnaFormaController glavnaFormaCont;
    private PrikazAnsambalaController paCont;
    private DodajAnsamblController daCont;
    private PrikazClanovaController pcCont;
    private DodajClanController dcCont;
    private Map<String, Object> parametri;

    private Coordinator() {
        parametri = new HashMap<>();
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

    public void otvoriGlavnuFormu() {
        glavnaFormaCont = new GlavnaFormaController(new GlavnaForma());
        glavnaFormaCont.otvoriFormu();
    }

    public void setAdmin(Administrator admin) {
        this.admin = admin;
    }

    public Administrator getAdmin() {
        return admin;
    }

    public void otvoriPrikazAnsamblFormu() {
        paCont = new PrikazAnsambalaController(new PrikazAnsambalaForma());
        paCont.otvoriFormu();
    }

    public void otvoriDodajAnsamblFormu() {
        daCont = new DodajAnsamblController(new DodajAnsamblForma());
        daCont.otvoriFormu(FormaMod.DODAJ);
    }

    public void dodajParam(String s, Object o) {
        parametri.put(s, o);
    }

    public Object vratiParam(String s) {
        return parametri.get(s);
    }

    public void otvoriIzmeniAnsamblFormu() {
        daCont = new DodajAnsamblController(new DodajAnsamblForma());
        daCont.otvoriFormu(FormaMod.IZMENI);
    }

    public void osveziFormu() {
        if (paCont != null) {
            paCont.osveziFormu();
        }
    }

    public void otvoriPrikazClanFormu() {
        pcCont = new PrikazClanovaController(new PrikazClanovaForma());
        pcCont.otvoriFormu();
    }

    public void osveziClanFormu() {
        if (pcCont != null) {
            pcCont.osveziFormu();
        }
    }

    public void otvoriDodajClanFormu() {
        dcCont = new DodajClanController(new DodajClanForma());
        dcCont.otvoriFormu(FormaMod.DODAJ);
    }

    public void otvoriIzmeniClanFormu() {
        dcCont = new DodajClanController(new DodajClanForma());
        dcCont.otvoriFormu(FormaMod.IZMENI);
    }

    public void osveziGlavnuFormu() {
        if (glavnaFormaCont != null) {
            glavnaFormaCont.osveziUcesceTabela();
        }
    }

    public void otvoriUpravljajZanrovimaFormu() {
        UpravljajZanrovimaController controller = new UpravljajZanrovimaController(new UpravljajZanrovimaForma(null, true));
        controller.open();
    }
}
