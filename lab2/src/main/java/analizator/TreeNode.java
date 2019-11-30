import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class TreeNode {
    //reference children nodes
    private List<TreeNode> children;

    public TreeNode(TreeNode... children) {
        this.children = Arrays.asList(children);
    }

    public TreeNode(ArrayList<TreeNode> children) {
        this.children = children;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public abstract void printTree(int depth);
}
