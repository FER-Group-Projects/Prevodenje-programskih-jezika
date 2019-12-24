import java.io.InputStream;
import java.util.Scanner;
import java.util.Stack;

public class TreeBuilder {

    public static Node buildTreeFromInput(InputStream inputStream){
        Scanner sc = new Scanner(inputStream);

        Stack<Node> nodeStack = new Stack<>();
        Stack<Integer> indentationStack = new Stack<>();
        Node root = null;

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String trimmedLine = line.trim();
            int currentIndentation = line.length() - trimmedLine.length();

            if (nodeStack.isEmpty()) {
                root = NodeFactory.getNode(trimmedLine);

                nodeStack.push(root);
                indentationStack.push(currentIndentation);

                continue;
            }

            while (indentationStack.peek() > currentIndentation - 1) {
                nodeStack.pop();
                indentationStack.pop();
            }

            Node newNode = NodeFactory.getNode(trimmedLine);

            nodeStack.peek().appendChild(newNode);
            newNode.setParent(nodeStack.peek());

            nodeStack.push(newNode);
            indentationStack.push(currentIndentation);
        }

        return root;
    }
    
}
