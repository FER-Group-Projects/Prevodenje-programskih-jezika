import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    protected List<Node> rightSide;
    protected int currentRightSideIndex;
    protected int rightSideType;
    // block table contains: local identifiers/variables and functions table
    protected BlockTable blockTable;

    // TODO: Je li ovo bolje nego da pamti samo BlockTable? -> napomena: vidi i odgovarajuce "parent" atribute u BlockTable i FunctionTable - to je sve povezano... - SREDITI!
    // parent Node
    protected Node parent;

    public abstract Node analyze();

    public abstract String toText();

    public Node() {
        rightSide = new ArrayList<>();
        currentRightSideIndex = 0;
    }

    public void appendChild(Node child) {
        rightSide.add(child);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(toText());
        sb.append(" ::= ");
        for (Node n : rightSide) {
            sb.append(n.toText());
        }
        return sb.toString();
    }

    public void errorHappened() {
        System.out.println(toString());
        System.exit(0);
    }
}
