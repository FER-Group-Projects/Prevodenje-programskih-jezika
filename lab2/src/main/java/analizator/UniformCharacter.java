public class UniformCharacter {
    private Symbol idSymbol;
    private int line;
    private String text;

    public UniformCharacter(Symbol idSymbol, int line, String text) {
        this.idSymbol = idSymbol;
        this.line = line;
        this.text = text;
    }

    public Symbol getIdSymbol() {
        return idSymbol;
    }

    public int getLine() {
        return line;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        if (idSymbol.equals(Symbol.EPSILON)) return Symbol.EPSILON.toString();
        return idSymbol.getSymbol() + " " + line + " " + text;
    }
}
