public class SemantickiAnalizator {

    private Node tree;

    public SemantickiAnalizator() {
        tree = TreeBuilder.buildTreeFromInput(System.in);
    }

    public void execute() {
        //TODO: dfs obilazak stabla sa vlastitim stogom
    }

    public static void main(String[] args) {
        SemantickiAnalizator semantickiAnalizator = new SemantickiAnalizator();
        semantickiAnalizator.execute();
    }
}
