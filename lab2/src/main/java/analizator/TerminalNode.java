package analizator;

public class TerminalNode extends TreeNode {
    //has no children nodes
    //UniformCharacter with terminal symbol
    private UniformCharacter character;

    public TerminalNode(UniformCharacter character) {
        this.character = character;
    }

    @Override
    public String toString() {
        return character.toString();
    }

    @Override
    public void printTree(int depth) {
        //output space 'depth' times
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append(" ");
        }
        System.out.print(sb.toString());
        //then the character
        System.out.println(character);
    }
}
