package analizator;

import java.util.Stack;

public class SA {
    private static UniformCharacterStream inputTape;
    private static Stack<PDAStackItem> pdaStack;
    private TreeNode tree;
    private static SADescriptor descriptor;

    private Stack<TreeNode> nodestack = new Stack<>();

    public static void main(String[] args) {
        //init stack
        pdaStack = new Stack<>();
        pdaStack.push(new PDAStackItem("0", Symbol.STACK_BOTTOM));
        //init UniformCharacterStream with stdin
        inputTape = new UniformCharacterStream(System.in);
        //deserialize sa-descriptor
        descriptor = DescriptorSerializer.deserialize();
        //do actions until done
        //and watch out for errors
    }
}
