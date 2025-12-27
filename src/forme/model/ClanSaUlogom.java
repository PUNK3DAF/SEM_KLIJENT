package forme.model;

import domen.ClanDrustva;

/**
 * Klasa koja čuva člana i njegovu uloga u ansamblu
 */
public class ClanSaUlogom {
    private ClanDrustva clan;
    private String uloga;

    public ClanSaUlogom(ClanDrustva clan, String uloga) {
        this.clan = clan;
        this.uloga = uloga;
    }

    public ClanDrustva getClan() {
        return clan;
    }

    public void setClan(ClanDrustva clan) {
        this.clan = clan;
    }

    public String getUloga() {
        return uloga;
    }

    public void setUloga(String uloga) {
        this.uloga = uloga;
    }

    @Override
    public String toString() {
        return String.valueOf(clan);
    }
}
