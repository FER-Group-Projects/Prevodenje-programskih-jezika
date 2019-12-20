import java.util.ArrayList;
import java.util.Stack;

public class SemantickiAnalizator {

    private Node tree;
    private Stack<Node> nodeStack = new Stack<>();

    public SemantickiAnalizator() {
        tree = TreeBuilder.buildTreeFromInput(System.in);
        nodeStack.push(tree);
    }

    public void execute() {
        dfs();
        functionTableCheck();
    }

    public void dfs() {
        System.out.println("Radim dfs obilazak stabla");
        while (!nodeStack.isEmpty()) {
            Node current = nodeStack.pop();
            Node next = current.analyze();
            if (next != null) {
                nodeStack.push(current);
                nodeStack.push(next);
            }
        }
    }

    public void functionTableCheck() {
        //TODO
        System.out.println("Radim provjere funkcija nakon obilaska");
    }

    public static void main(String[] args) {
        SemantickiAnalizator semantickiAnalizator = new SemantickiAnalizator();
        semantickiAnalizator.execute();
    }
}
