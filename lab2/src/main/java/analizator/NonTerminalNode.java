package analizator;

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
        //TODO
        return null;
    }
}
