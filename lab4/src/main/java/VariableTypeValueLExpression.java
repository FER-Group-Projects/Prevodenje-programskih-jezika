//  contains variable type and value

public  class VariableTypeValueLExpression {
    private Type tip;
    private String vrijednost;

    public VariableTypeValueLExpression(Type tip, String vrijednost) {
        this.tip = tip;
        this.vrijednost = vrijednost;
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
        return (tip == Type.INT || tip == Type.CHAR) ? 1 : 0;
    }


}