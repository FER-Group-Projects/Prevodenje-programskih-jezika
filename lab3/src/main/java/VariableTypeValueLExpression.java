//  contains variable type and value

public  class VariableTypeValueLExpression {
    private Type tip;
    private String vrijednost;
    private int lIzraz;

    public VariableTypeValueLExpression(Type tip, String vrijednost, int lIzraz) {
        this.tip = tip;
        this.vrijednost = vrijednost;
        this.lIzraz = lIzraz;
    }

    public Type getTip() {
        return tip;
    }

    public void setTip(Type tip) {
        this.tip = tip;
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
}