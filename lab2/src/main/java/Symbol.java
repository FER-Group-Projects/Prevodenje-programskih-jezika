import java.util.Objects;

public class Symbol {

    public static final Symbol EPSILON = new Symbol("$");

    private final String symbol;
    private final boolean isTerminal;

    public Symbol(String symbol) {
        this(symbol, !symbol.startsWith("<"));
    }

    public Symbol(String symbol, boolean isTerminal) {
        this.symbol = symbol;
        this.isTerminal = isTerminal;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol1 = (Symbol) o;
        return Objects.equals(symbol, symbol1.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}
