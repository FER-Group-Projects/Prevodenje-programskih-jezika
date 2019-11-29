package analizator;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class TreeNode {
    //reference children nodes
    private ArrayList<TreeNode> children;

    public TreeNode(TreeNode... children) {
        this.children = (ArrayList<TreeNode>) Arrays.asList(children);
    }

    public TreeNode(ArrayList<TreeNode> children) {
        this.children = children;
    }

    public ArrayList<TreeNode> getChildren() {
        return children;
    }

    public abstract void printTree(int depth);
}
