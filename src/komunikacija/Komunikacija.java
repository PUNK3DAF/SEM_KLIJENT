/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package komunikacija;

import domen.Administrator;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author vldmrk
 */
public class Komunikacija {

    private Socket soket;
    private Posiljalac posiljalac;
    private Primalac primalac;
    private static Komunikacija instanca;

    private Komunikacija() {
    }

    public static Komunikacija getInstanca() {
        if (instanca == null) {
            instanca = new Komunikacija();
        }
        return instanca;
    }

    public void konekcija() {
        try {
            soket = new Socket("localhost", 9000);
            posiljalac = new Posiljalac(soket);
            primalac = new Primalac(soket);
        } catch (IOException ex) {
            System.out.println("SERVER NIJE POVEZAN");
        }
    }

    public Administrator login(String user, String pass) {
        Administrator a = new Administrator();
        a.setAdminUsername(user);
        a.setAdminPassword(pass);
        Zahtev z = new Zahtev(Operacije.LOGIN, a);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        return (Administrator) odg.getOdgovor();
    }

}
