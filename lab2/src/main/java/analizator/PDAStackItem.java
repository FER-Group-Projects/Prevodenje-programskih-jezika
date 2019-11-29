package analizator;

public class PDAStackItem {
    private String state;
    private Symbol stackSymbol;

    private boolean isUniformCharacter;
    private UniformCharacter uc;

    public PDAStackItem(String state, Symbol stackSymbol, boolean isUniformCharacter, UniformCharacter uc) {
        this.state = state;
        this.stackSymbol = stackSymbol;
        this.isUniformCharacter = isUniformCharacter;
        this.uc = uc;
    }

    public PDAStackItem(String state, Symbol stackSymbol) {
        this(state, stackSymbol, false, null);
    }

    public String getState() {
        return state;
    }

    public Symbol getStackSymbol() {
        if (isUniformCharacter) return uc.getIdSymbol();
        return stackSymbol;
    }

    public UniformCharacter getUc() {
        return uc;
    }

    public boolean isUniformCharacter() {
        return isUniformCharacter;
    }

    @Override
    public String toString() {
        return "PDAStackItem{" +
                "state='" + state + '\'' +
                ", stackSymbol=" + stackSymbol +
                ", isUniformCharacter=" + isUniformCharacter +
                ", uc=" + uc +
                '}';
    }
}
