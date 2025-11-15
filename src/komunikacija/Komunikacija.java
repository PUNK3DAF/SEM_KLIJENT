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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

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
        List<Ansambl> ansambli;

        posiljalac.posalji(z);

        Odgovor odg = (Odgovor) primalac.primi();
        ansambli = (List<Ansambl>) odg.getOdgovor();
        return ansambli;
    }

    public void obrisiAnsambl(Ansambl a) throws Exception {
        Zahtev z = new Zahtev(Operacije.OBRISI_ANSAMBL, a);
        posiljalac.posalji(z);

        Odgovor odg = (Odgovor) primalac.primi();
        if (odg.getOdgovor() == null) {
            System.out.println("USPEH");
        } else {
            Exception serverEx;
            if (odg.getOdgovor() instanceof Exception) {
                serverEx = (Exception) odg.getOdgovor();
            } else {
                serverEx = new Exception(String.valueOf(odg.getOdgovor()));
            }
            String poruka = serverEx.getMessage() != null ? serverEx.getMessage() : String.valueOf(odg.getOdgovor());
            JOptionPane.showMessageDialog(null, poruka, "Greska", JOptionPane.ERROR_MESSAGE);
            throw serverEx;
        }
    }

    public void dodajAnsambl(Ansambl a) {
        Zahtev z = new Zahtev(Operacije.DODAJ_ANSAMBL, a);
        posiljalac.posalji(z);

        Odgovor odg = (Odgovor) primalac.primi();
        if (odg.getOdgovor() == null) {
            System.out.println("USPEH");
        } else {
            System.out.println("GRESKA");
        }
    }

    public void azurirajAnsambl(Ansambl a) throws Exception {
        Zahtev z = new Zahtev(Operacije.AZURIRAJ_ANSAMBL, a);
        posiljalac.posalji(z);

        Odgovor odg = (Odgovor) primalac.primi();
        if (odg.getOdgovor() == null) {
            System.out.println("USPEH");
            coordinator.Coordinator.getInstanca().osveziFormu();
        } else {
            Exception serverEx;
            if (odg.getOdgovor() instanceof Exception) {
                serverEx = (Exception) odg.getOdgovor();
            } else {
                serverEx = new Exception(String.valueOf(odg.getOdgovor()));
            }
            throw serverEx;
        }
    }

    public List<ClanDrustva> ucitajClanove() {
        Zahtev z = new Zahtev(Operacije.UCITAJ_CLANOVE, null);
        List<ClanDrustva> clanovi;

        posiljalac.posalji(z);

        Odgovor odg = (Odgovor) primalac.primi();
        clanovi = (List<ClanDrustva>) odg.getOdgovor();
        if (clanovi == null) {
            return new ArrayList<>();
        }
        return clanovi;
    }

    public void obrisiClan(ClanDrustva c) throws Exception {
        Zahtev z = new Zahtev(Operacije.OBRISI_CLAN, c);
        posiljalac.posalji(z);

        Odgovor odg = (Odgovor) primalac.primi();
        if (odg.getOdgovor() == null) {
            System.out.println("USPEH");
        } else {
            Exception serverEx;
            if (odg.getOdgovor() instanceof Exception) {
                serverEx = (Exception) odg.getOdgovor();
            } else {
                serverEx = new Exception(String.valueOf(odg.getOdgovor()));
            }
            throw serverEx;
        }
    }

    public void dodajClan(ClanDrustva c) throws Exception {
        Zahtev z = new Zahtev(Operacije.DODAJ_CLAN, c);
        posiljalac.posalji(z);

        Odgovor odg = (Odgovor) primalac.primi();
        if (odg.getOdgovor() == null) {
            JOptionPane.showMessageDialog(null, "Sistem je kreirao clana drustva", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            coordinator.Coordinator.getInstanca().osveziClanFormu();
        } else {
            Exception serverEx;
            if (odg.getOdgovor() instanceof Exception) {
                serverEx = (Exception) odg.getOdgovor();
            } else {
                serverEx = new Exception(String.valueOf(odg.getOdgovor()));
            }
            String poruka = serverEx.getMessage() != null ? serverEx.getMessage() : String.valueOf(odg.getOdgovor());
            JOptionPane.showMessageDialog(null, "Sistem ne moze da kreira clana drustva.\nRazlog: " + poruka, "Greska", JOptionPane.ERROR_MESSAGE);
            throw serverEx;
        }
    }

    public void azurirajClan(ClanDrustva c) throws Exception {
        Zahtev z = new Zahtev(Operacije.AZURIRAJ_CLAN, c);
        posiljalac.posalji(z);

        Odgovor odg = (Odgovor) primalac.primi();
        if (odg.getOdgovor() == null) {
            System.out.println("USPEH: clan azuriran");
            coordinator.Coordinator.getInstanca().osveziClanFormu();
        } else {
            Exception serverEx;
            if (odg.getOdgovor() instanceof Exception) {
                serverEx = (Exception) odg.getOdgovor();
            } else {
                serverEx = new Exception(String.valueOf(odg.getOdgovor()));
            }
            throw serverEx;
        }
    }

    public List<Ucesce> ucitajUcesca() {
        Zahtev z = new Zahtev(Operacije.UCITAJ_UCESCA, null);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        List<Ucesce> lista = (List<Ucesce>) odg.getOdgovor();
        if (lista == null) {
            return new ArrayList<>();
        }
        return lista;
    }

    public void dodajAnsamblSaSastavom(Ansambl a) throws Exception {
        Zahtev z = new Zahtev(Operacije.DODAJ_ANSAMBL_SA_SASTAVOM, a);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        if (odg.getOdgovor() == null) {
            System.out.println("USPEH - ansambl dodat sa sastavom");
            coordinator.Coordinator.getInstanca().osveziGlavnuFormu();
        } else {
            Exception serverEx;
            if (odg.getOdgovor() instanceof Exception) {
                serverEx = (Exception) odg.getOdgovor();
            } else {
                serverEx = new Exception(String.valueOf(odg.getOdgovor()));
            }
            throw serverEx;
        }
    }

    public void azurirajSastavAnsambla(Ansambl a) throws Exception {
        Zahtev z = new Zahtev(Operacije.AZURIRAJ_SASTAV_ANSAMBLA, a);
        posiljalac.posalji(z);
        Odgovor odg = (Odgovor) primalac.primi();
        if (odg.getOdgovor() == null) {
            System.out.println("USPEH - sastav ansambla azuriran");
            coordinator.Coordinator.getInstanca().osveziFormu();
            coordinator.Coordinator.getInstanca().osveziGlavnuFormu();
        } else {
            Exception serverEx;
            if (odg.getOdgovor() instanceof Exception) {
                serverEx = (Exception) odg.getOdgovor();
            } else {
                serverEx = new Exception(String.valueOf(odg.getOdgovor()));
            }
            throw serverEx;
        }
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

}
