import java.util.List;

public abstract class Node {
    private List<Node> rightSide;
    private int currentRightSideIndex;
    private int rightSideType;

    public abstract Node analyze();
    public abstract String toText();

    @Override
    public String toString() {
        //TODO: ispisi produkciju u BNF obliku
        return null;
    }
}
