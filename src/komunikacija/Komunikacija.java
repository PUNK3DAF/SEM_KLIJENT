/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package komunikacija;

import domen.Administrator;
import domen.Ansambl;
import domen.ClanDrustva;
import domen.Ucesce;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

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
        Zahtev z = new Zahtev(Operacije.ADMIN_LOGIN, a);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();

        if (odg.getOdgovor() instanceof Exception) {
            return null;
        }

        return (Administrator) odg.getOdgovor();
    }

    public List<Ansambl> ucitajAnsamble() {
        Zahtev z = new Zahtev(Operacije.UCITAJ_ANSAMBLE, null);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        return (List<Ansambl>) odg.getOdgovor();
    }

    public Ansambl ucitajAnsambl(int ansamblId) throws Exception {
        Zahtev z = new Zahtev();
        z.setOperacija(Operacije.UCITAJ_ANSAMBL);
        Ansambl probe = new Ansambl();
        probe.setAnsamblID(ansamblId);
        z.setParametar(probe);
        posiljalac.posalji(z);

        Odgovor o = (Odgovor) primalac.primi();
        if (o.getOdgovor() instanceof Exception) {
            throw (Exception) o.getOdgovor();
        }
        return (Ansambl) o.getOdgovor();
    }

    public void obrisiAnsambl(Ansambl a) throws Exception {
        Zahtev z = new Zahtev(Operacije.OBRISI_ANSAMBL, a);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        
        if (odg.getOdgovor() != null) {
            throw unwrapException(odg.getOdgovor());
        }
    }

    public void dodajAnsambl(Ansambl a) throws Exception {
        Zahtev z = new Zahtev(Operacije.KREIRAJ_ANSAMBL, a);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        
        if (odg.getOdgovor() != null) {
            throw unwrapException(odg.getOdgovor());
        }
    }

    public void azurirajAnsambl(Ansambl a) throws Exception {
        Zahtev z = new Zahtev(Operacije.AZURIRAJ_ANSAMBL, a);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        
        if (odg.getOdgovor() != null) {
            throw unwrapException(odg.getOdgovor());
        }
        coordinator.Coordinator.getInstanca().osveziFormu();
    }

    public List<ClanDrustva> ucitajClanove() {
        Zahtev z = new Zahtev(Operacije.UCITAJ_CLANOVE, null);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        
        List<ClanDrustva> clanovi = (List<ClanDrustva>) odg.getOdgovor();
        return clanovi == null ? new ArrayList<>() : clanovi;
    }

    public void obrisiClan(ClanDrustva c) throws Exception {
        Zahtev z = new Zahtev(Operacije.OBRISI_CLAN, c);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        
        if (odg.getOdgovor() != null) {
            throw unwrapException(odg.getOdgovor());
        }
    }

    public void dodajClan(ClanDrustva c) throws Exception {
        Zahtev z = new Zahtev(Operacije.KREIRAJ_CLAN, c);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        
        if (odg.getOdgovor() != null) {
            throw unwrapException(odg.getOdgovor());
        }
    }

    public void azurirajClan(ClanDrustva c) throws Exception {
        Zahtev z = new Zahtev(Operacije.AZURIRAJ_CLAN, c);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        
        if (odg.getOdgovor() != null) {
            throw unwrapException(odg.getOdgovor());
        }
        coordinator.Coordinator.getInstanca().osveziClanFormu();
    }

    public List<Ucesce> ucitajUcesca() {
        Zahtev z = new Zahtev(Operacije.UCITAJ_UCESCA, null);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        
        List<Ucesce> lista = (List<Ucesce>) odg.getOdgovor();
        return lista == null ? new ArrayList<>() : lista;
    }

    public void dodajAnsamblSaSastavom(Ansambl a) throws Exception {
        Zahtev z = new Zahtev(Operacije.KREIRAJ_ANSAMBL_SA_SASTAVOM, a);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        
        if (odg.getOdgovor() != null) {
            throw unwrapException(odg.getOdgovor());
        }
        coordinator.Coordinator.getInstanca().osveziGlavnuFormu();
    }

    public void azurirajSastavAnsambla(Ansambl a) throws Exception {
        Zahtev z = new Zahtev(Operacije.AZURIRAJ_SASTAV_ANSAMBLA, a);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        
        if (odg.getOdgovor() != null) {
            throw unwrapException(odg.getOdgovor());
        }
        coordinator.Coordinator.getInstanca().osveziFormu();
        coordinator.Coordinator.getInstanca().osveziGlavnuFormu();
    }

    public List<ClanDrustva> nadjiClanaDrustva(String vrednost) throws Exception {
        Zahtev z = new Zahtev();
        z.setOperacija(Operacije.NADJI_CLANOVE);
        z.setParametar(vrednost);
        posiljalac.posalji(z);
        Odgovor o = (Odgovor) primalac.primi();
        
        if (o.getOdgovor() instanceof Exception) {
            throw (Exception) o.getOdgovor();
        }
        return (List<ClanDrustva>) o.getOdgovor();
    }

    public ClanDrustva ucitajClanaDrustva(int clanId) throws Exception {
        Zahtev z = new Zahtev();
        z.setOperacija(Operacije.UCITAJ_CLANA);
        ClanDrustva probe = new ClanDrustva();
        probe.setClanID(clanId);
        z.setParametar(probe);
        posiljalac.posalji(z);
        Odgovor o = (Odgovor) primalac.primi();
        
        if (o.getOdgovor() instanceof Exception) {
            throw (Exception) o.getOdgovor();
        }
        return (ClanDrustva) o.getOdgovor();
    }

    public List<Ansambl> nadjiAnsambla(String vrednost) throws Exception {
        Zahtev z = new Zahtev();
        z.setOperacija(Operacije.NADJI_ANSAMBL);
        z.setParametar(vrednost);
        posiljalac.posalji(z);
        Odgovor o = (Odgovor) primalac.primi();
        
        if (o.getOdgovor() instanceof Exception) {
            throw (Exception) o.getOdgovor();
        }
        return (List<Ansambl>) o.getOdgovor();
    }

    private Exception unwrapException(Object response) {
        if (response instanceof Exception) {
            return (Exception) response;
        }
        return new Exception(String.valueOf(response));
    }
}
