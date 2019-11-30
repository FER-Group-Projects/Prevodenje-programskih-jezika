import java.util.ArrayList;

public class NonTerminalNode extends TreeNode {
    //has children nodes
    //Non terminal symbol
    private Symbol symbol;

    public NonTerminalNode(Symbol symbol, TreeNode... children) {
        super(children);
        this.symbol = symbol;
    }

    public NonTerminalNode(Symbol symbol, ArrayList<TreeNode> children) {
        super(children);
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol.toString();
    }

    @Override
    public void printTree(int depth) {
        //output space 'depth' times
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append(" ");
        }
        System.out.print(sb.toString());
        //print symbol
        System.out.println(symbol);
        //then the children
        for (TreeNode node : super.getChildren()) {
            node.printTree(depth + 1);
        }
    }
}
