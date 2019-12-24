import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    protected List<Node> rightSide;
    protected int currentRightSideIndex;
    protected int rightSideType;

    protected Properties properties;
    // block table contains: local identifiers/variables
    protected BlockTable blockTable;


    protected Node parent;

    public abstract Node analyze();
    public abstract String toText();
    public abstract void determineRightSideType();

    public Node() {
        rightSide = new ArrayList<>();
        currentRightSideIndex = 0;
        rightSideType = -1;
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
            sb.append(n.toText()).append(" ");
        }
        return sb.toString();
    }

    public void errorHappened() {
        System.out.println(toString());
        System.exit(0);
    }


    public String getName() {
        return toText();
    }

    public void printTree(int depth) {
        //output space 'depth' times
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append(" ");
        }
        System.out.print(sb.toString());
        //print symbol
        System.out.println(getName());
        //then the children
        for (Node node : rightSide) {
            node.printTree(depth + 1);
        }
    }

    public void setBlockTable(BlockTable blockTable) {
        this.blockTable = blockTable;
    }

    public BlockTable getBlockTable() {
        return blockTable;
    }
  
    public void setParent(Node parent) {
        this.parent = parent;
    }
}
