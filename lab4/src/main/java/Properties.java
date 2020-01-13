import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Properties {
    private Type tip;
    private String ime;
    private String vrijednost;
    private int lIzraz;
    private Type pov;
    private List<Type> tipovi = new ArrayList<>();
    private List<String> imena = new ArrayList<>();
    private Type ntip;
    private int brElem;

    public Type getTip() {
        return tip;
    }

    public void setTip(Type tip) {
        this.tip = tip;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(String vrijednost) {
        this.vrijednost = vrijednost;
    }

    public int getlIzraz() {
        return lIzraz;
    }

    public void setlIzraz(int lIzraz) {
        this.lIzraz = lIzraz;
    }

    public Type getPov() {
        return pov;
    }

    public void setPov(Type pov) {
        this.pov = pov;
    }

    public List<Type> getTipovi() {
        return tipovi;
    }

    public void setTipovi(List<Type> tipovi) {
        this.tipovi = tipovi;
    }

    public List<String> getImena() {
        return imena;
    }

    public void setImena(List<String> imena) {
        this.imena = imena;
    }

    public Type getNtip() {
        return ntip;
    }

    public void setNtip(Type ntip) {
        this.ntip = ntip;
    }

    public int getBrElem() {
        return brElem;
    }

    public void setBrElem(int brElem) {
        this.brElem = brElem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Properties that = (Properties) o;
        return lIzraz == that.lIzraz &&
                brElem == that.brElem &&
                tip == that.tip &&
                Objects.equals(ime, that.ime) &&
                Objects.equals(vrijednost, that.vrijednost) &&
                pov == that.pov &&
                Objects.equals(tipovi, that.tipovi) &&
                Objects.equals(imena, that.imena) &&
                ntip == that.ntip;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tip, ime, vrijednost, lIzraz, pov, tipovi, imena, ntip, brElem);
    }

    @Override
    public String toString() {
        return "Properties{" +
                "tip=" + tip +
                ", ime='" + ime + '\'' +
                ", vrijednost='" + vrijednost + '\'' +
                ", lIzraz=" + lIzraz +
                ", pov=" + pov +
                ", tipovi=" + tipovi +
                ", imena=" + imena +
                ", ntip=" + ntip +
                ", brElem=" + brElem +
                '}';
    }
}
