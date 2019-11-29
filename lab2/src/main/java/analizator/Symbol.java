package analizator;

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
	public int hashCode() {
		return Objects.hash(isTerminal, symbol);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Symbol))
			return false;
		Symbol other = (Symbol) obj;
		return isTerminal == other.isTerminal && Objects.equals(symbol, other.symbol);
	}
    
}
