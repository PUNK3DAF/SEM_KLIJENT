/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package komunikacija;

import domen.Administrator;
import domen.Ansambl;
import domen.ClanDrustva;
import domen.Dogadjaj;
import domen.Mesto;
import domen.Ucesce;
import domen.Zanr;
import domen.Uloga;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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

    public void kreirajAnsambl(Ansambl a) throws Exception {
        Zahtev z = new Zahtev(Operacije.KREIRAJ_ANSAMBL, a);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        
        if (odg.getOdgovor() != null) {
            throw unwrapException(odg.getOdgovor());
        }
        coordinator.Coordinator.getInstanca().osveziGlavnuFormu();
    }

    public void izmenaAnsambla(Ansambl a) throws Exception {
        Zahtev z = new Zahtev(Operacije.IZMENA_ANSAMBLA, a);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        
        if (odg.getOdgovor() != null) {
            throw unwrapException(odg.getOdgovor());
        }
        coordinator.Coordinator.getInstanca().osveziFormu();
        coordinator.Coordinator.getInstanca().osveziGlavnuFormu();
    }

    public List<ClanDrustva> ucitajClanove() {
        Zahtev z = new Zahtev(Operacije.UCITAJ_CLANOVE, null);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        
        List<ClanDrustva> clanovi = (List<ClanDrustva>) odg.getOdgovor();
        return clanovi == null ? new ArrayList<>() : clanovi;
    }

    public void obrisiClanaDrustva(ClanDrustva c) throws Exception {
        Zahtev z = new Zahtev(Operacije.OBRISI_CLANA_DRUSTVA, c);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        
        if (odg.getOdgovor() != null) {
            throw unwrapException(odg.getOdgovor());
        }
    }

    public void kreirajClanaDrustva(ClanDrustva c) throws Exception {
        Zahtev z = new Zahtev(Operacije.KREIRAJ_CLANA_DRUSTVA, c);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        
        if (odg.getOdgovor() != null) {
            throw unwrapException(odg.getOdgovor());
        }
    }

    public void izmeniClanaDrustva(ClanDrustva c) throws Exception {
        Zahtev z = new Zahtev(Operacije.IZMENI_CLANA_DRUSTVA, c);
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

        if (odg.getOdgovor() instanceof Exception) {
            return new ArrayList<>();
        }

        List<Ucesce> lista = (List<Ucesce>) odg.getOdgovor();
        return lista == null ? new ArrayList<>() : lista;
    }

    public void kreirajZanr(Zanr z) throws Exception {
        Zahtev zahtev = new Zahtev(Operacije.KREIRAJ_ZANR, z);
        posiljalac.posalji(zahtev);
        Odgovor odg = (Odgovor) primalac.primi();
        
        if (odg.getOdgovor() != null) {
            throw unwrapException(odg.getOdgovor());
        }
    }

    public List<Zanr> ucitajZanrove() {
        Zahtev z = new Zahtev(Operacije.UCITAJ_ZANROVE, null);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        
        List<Zanr> zanrovi = (List<Zanr>) odg.getOdgovor();
        return zanrovi == null ? new ArrayList<>() : zanrovi;
    }

    public void kreirajUlogu(Uloga u) throws Exception {
        Zahtev zahtev = new Zahtev(Operacije.KREIRAJ_ULOGU, u);
        posiljalac.posalji(zahtev);
        Odgovor odg = (Odgovor) primalac.primi();

        if (odg.getOdgovor() != null) {
            throw unwrapException(odg.getOdgovor());
        }
    }

    public List<Uloga> ucitajUloge() {
        Zahtev z = new Zahtev(Operacije.UCITAJ_ULOGE, null);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();

        List<Uloga> uloge = (List<Uloga>) odg.getOdgovor();
        return uloge == null ? new ArrayList<>() : uloge;
    }

    public void kreirajMesto(Mesto m) throws Exception {
        Zahtev zahtev = new Zahtev(Operacije.KREIRAJ_MESTO, m);
        posiljalac.posalji(zahtev);
        Odgovor odg = (Odgovor) primalac.primi();

        if (odg.getOdgovor() != null) {
            throw unwrapException(odg.getOdgovor());
        }
    }

    public List<Mesto> ucitajMesta() {
        Zahtev z = new Zahtev(Operacije.UCITAJ_MESTA, null);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();

        List<Mesto> mesta = (List<Mesto>) odg.getOdgovor();
        return mesta == null ? new ArrayList<>() : mesta;
    }

    public void kreirajDogadjaj(Dogadjaj d) throws Exception {
        Zahtev zahtev = new Zahtev(Operacije.KREIRAJ_DOGADJAJ, d);
        posiljalac.posalji(zahtev);
        Odgovor odg = (Odgovor) primalac.primi();

        if (odg.getOdgovor() != null) {
            throw unwrapException(odg.getOdgovor());
        }
    }

    public List<Dogadjaj> ucitajDogadjaje() {
        Zahtev z = new Zahtev(Operacije.UCITAJ_DOGADJAJE, null);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();

        List<Dogadjaj> dogadjaji = (List<Dogadjaj>) odg.getOdgovor();
        return dogadjaji == null ? new ArrayList<>() : dogadjaji;
    }

    public void izmeniDogadjaj(Dogadjaj d) throws Exception {
        Zahtev zahtev = new Zahtev(Operacije.IZMENI_DOGADJAJ, d);
        posiljalac.posalji(zahtev);
        Odgovor odg = (Odgovor) primalac.primi();

        if (odg.getOdgovor() != null) {
            throw unwrapException(odg.getOdgovor());
        }
    }

    public void obrisiDogadjaj(Dogadjaj d) throws Exception {
        Zahtev zahtev = new Zahtev(Operacije.OBRISI_DOGADJAJ, d);
        posiljalac.posalji(zahtev);
        Odgovor odg = (Odgovor) primalac.primi();

        if (odg.getOdgovor() != null) {
            throw unwrapException(odg.getOdgovor());
        }
    }

    public List<ClanDrustva> nadjiClanaDrustva(String vrednost) throws Exception {
        Zahtev z = new Zahtev();
        z.setOperacija(Operacije.NADJI_CLANA_DRUSTVA);
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
        z.setOperacija(Operacije.UCITAJ_CLANA_DRUSTVA);
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

    public List<Ansambl> nadjiAnsambl(String vrednost) throws Exception {
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
