package analizator;

public class PDAStackItem {
    private String state;
    private Symbol stackSymbol;

    public PDAStackItem(String state, Symbol stackSymbol) {
        this.state = state;
        this.stackSymbol = stackSymbol;
    }

    public String getState() {
        return state;
    }

    public Symbol getStackSymbol() {
        return stackSymbol;
    }
}
