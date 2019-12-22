import java.util.*;

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
            Node current = nodeStack.peek();
            Node next = current.analyze();
            if (next != null) {
                nodeStack.push(next);
            } else {
		nodeStack.pop();
		} 
        }
    }

    public void functionTableCheck() {
        //TODO
        System.out.println("Radim provjere funkcija nakon obilaska");

        if (!containsMainFunction()) {
            System.out.println("main");
            System.exit(1);
        }

        if (!allDeclaredFunctionsDefined(tree)) {
            System.out.println("funkcija");
            System.exit(1);
        }

    }

    public static void main(String[] args) {
        SemantickiAnalizator semantickiAnalizator = new SemantickiAnalizator();
        semantickiAnalizator.execute();
    }



    //metoda containsMainFunction - specijalno za main
    private boolean containsMainFunction() {
        List<String> mainInputTypes = Arrays.asList("void");
        return FunctionTable.containsFunction("main", "int", mainInputTypes);
    }

    //metoda provjerava jesu li sve deklarirane funkcije u cijelom programu i definirane
    private boolean allDeclaredFunctionsDefined(Node root) {

        if (root == null)
            return true;

        Collection<Function> functionsDeclared = FunctionTable.functionNameToInOutTypeMap.values();
        for (Function function : functionsDeclared) {
            if (!function.isDefined())
                return false;
        }

        List<Node> children = root.rightSide;
        for (Node child : children)
            allDeclaredFunctionsDefined(child);

        return true;
    }
}
