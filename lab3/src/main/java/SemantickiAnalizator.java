import java.util.*;

public class SemantickiAnalizator {

    private Node tree;
    private Stack<Node> nodeStack = new Stack<>();
    private BlockTable globalScope = new BlockTable();

    public SemantickiAnalizator() {
        tree = TreeBuilder.buildTreeFromInput(System.in);
        tree.setBlockTable(globalScope);
        nodeStack.push(tree);
    }

    public void execute() {
        dfs();
        functionTableCheck();
    }

    public void dfs() {
        while (!nodeStack.isEmpty()) {
            Node current = nodeStack.peek();
            Node next = current.analyze();
            if (next != null) {
                next.setBlockTable(current.getBlockTable());
                nodeStack.push(next);
            } else {
                nodeStack.pop();
            }
        }
    }

    public void functionTableCheck() {
        if (!containsMainFunction()) {
            System.out.println("main");
            System.exit(1);
        }

        if (!allDeclaredFunctionsDefined()) {
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
    private boolean allDeclaredFunctionsDefined() {

        Collection<Function> functionsDeclared = FunctionTable.functionNameToInOutTypeMap.values();
        for (Function function : functionsDeclared) {
            if (!function.isDefined())
                return false;
        }

        return true;
    }
}
