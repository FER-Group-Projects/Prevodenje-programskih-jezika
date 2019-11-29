package analizator;

import java.util.List;
import java.util.Objects;

public class GrammarRule {

    private final Symbol from;
    private final List<Symbol> toList;

    public GrammarRule(Symbol from, List<Symbol> toList) {
        this.from = from;
        this.toList = toList;
    }

    public Symbol getFrom() {
        return from;
    }

    public List<Symbol> getToList() {
        return toList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrammarRule that = (GrammarRule) o;
        return Objects.equals(from, that.from) &&
                Objects.equals(toList, that.toList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, toList);
    }

	@Override
	public String toString() {
		return "GrammarRule [from=" + from + ", toList=" + toList + "]";
	}
    
}
