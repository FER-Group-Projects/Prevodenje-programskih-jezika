package analizator;

import java.util.ArrayList;
import java.util.Arrays;

public class TreeNode {
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

    public static void printTree(TreeNode root, int depth) {
        //TODO
    }
}
