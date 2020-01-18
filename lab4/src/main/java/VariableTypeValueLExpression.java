//  contains variable type and value

public  class VariableTypeValueLExpression {
    private Type tip;
    private String vrijednost;
    private int offset;
    private int size;

    public VariableTypeValueLExpression(Type tip, String vrijednost) {
        this(tip, vrijednost, 0, 4);
    }

    public VariableTypeValueLExpression(Type tip, String vrijednost, int offset, int size) {
        this.tip = tip;
        this.vrijednost = vrijednost;
        this.offset = offset;
        this.size = size;
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


    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}