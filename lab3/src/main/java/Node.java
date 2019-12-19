import java.util.List;

public abstract class Node {
    protected List<Node> rightSide;
    protected int currentRightSideIndex;
    protected int rightSideType;

    public abstract Node analyze();
    public abstract String toText();

    @Override
    public String toString() {
        //TODO: ispisi produkciju u BNF obliku
        return null;
    }
}
