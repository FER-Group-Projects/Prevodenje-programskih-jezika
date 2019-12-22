import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    protected List<Node> rightSide;
    protected int currentRightSideIndex;
    protected int rightSideType;

    protected Properties properties;
    // block table contains: parent node and local identifiers table
    protected BlockTable blockTable;

    public abstract Node analyze();

    public abstract String toText();

    public Node() {
        rightSide = new ArrayList<>();
        currentRightSideIndex = 0;
        properties = new Properties();
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

    public String getName(){
        return toText();
    }
}
