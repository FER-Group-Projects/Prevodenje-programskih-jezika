import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LR1Item {

    private static final String SEPARATOR = "###";

    private final GrammarRule grammarRule;
    private final int dotIndex;
    private final Symbol after;

    private String cachedToString;

    public LR1Item(GrammarRule grammarRule, int dotIndex, Symbol after) {
        this.grammarRule = grammarRule;
        this.dotIndex = dotIndex;
        this.after = after;
    }

    public static LR1Item parseItem(String toParse) {
        String[] parts = toParse.split(SEPARATOR);

        if (parts.length != 4) throw new IllegalArgumentException("Cannot parse '" + toParse + "', has too many separators.");

        Symbol from = new Symbol(parts[0]);
        List<Symbol> toList = Stream.of(parts[1].split(",")).map(Symbol::new).collect(Collectors.toList());
        int dotIndex = Integer.parseInt(parts[2]);
        Symbol after = new Symbol(parts[3]);

        return new LR1Item(new GrammarRule(from, toList), dotIndex, after);
    }
    
    public boolean isFinalItem() {
    	return dotIndex == grammarRule.getToList().size();
    }

    public Symbol getFrom() {
        return grammarRule.getFrom();
    }

    public List<Symbol> getToList() {
        return grammarRule.getToList();
    }

    public int getDotIndex() {
        return dotIndex;
    }

    public Symbol getAfter() {
        return after;
    }
    
    public GrammarRule getGrammarRule() {
		return grammarRule;
	}

    @Override
    public String toString() {
        if (cachedToString != null) return cachedToString;

        cachedToString = grammarRule.getFrom() + SEPARATOR +
                grammarRule.getToList().stream().map(Symbol::toString).collect(Collectors.joining(",")) + SEPARATOR +
                dotIndex + SEPARATOR + after.toString();

        return cachedToString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LR1Item lrItem = (LR1Item) o;
        return dotIndex == lrItem.dotIndex &&
                Objects.equals(grammarRule, lrItem.grammarRule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grammarRule, dotIndex);
    }
}
